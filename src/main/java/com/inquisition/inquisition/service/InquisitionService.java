package com.inquisition.inquisition.service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordFullWithCaseId;
import com.inquisition.inquisition.model.accusation.AccusationRecordWithCasePayload;
import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.church.Church;
import com.inquisition.inquisition.model.inquisition.CurrentInquisitionProcessPayload;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessIdContainer;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessPayload;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.inquisition.StartInquisitionProcessRepContainer;
import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.payload.PayloadWithData;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.repository.AccusationProcessRepository;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.BibleRepository;
import com.inquisition.inquisition.repository.ChurchRepository;
import com.inquisition.inquisition.repository.InquisitionProcessRepository;
import com.inquisition.inquisition.repository.LocalityRepository;
import com.inquisition.inquisition.repository.OfficialRepository;
import com.inquisition.inquisition.repository.PersonRepository;
import com.inquisition.inquisition.repository.QueryFetchHelper;
import com.inquisition.inquisition.security.UserDetailsServiceImpl;
import com.inquisition.inquisition.utils.InquisitionProcessConverter;
import com.nimbusds.jose.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.utils.InquisitionProcessConverter.convertToCurrentPayload;
import static com.inquisition.inquisition.utils.Messages.INCORRECT_REQUEST;

@Service
public class InquisitionService {
    private final InquisitionProcessRepository inquisitionProcessRepository;
    private final AccusationRecordRepository accusationRecordRepository;
    private final AccusationProcessRepository accusationProcessRepository;
    private final OfficialRepository officialRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final BibleRepository bibleRepository;
    private final LocalityRepository localityRepository;
    private final PersonRepository personRepository;
    @Autowired
    private ChurchRepository churchRepository;

    public InquisitionService(InquisitionProcessRepository inquisitionProcessRepository,
                              AccusationRecordRepository accusationRecordRepository,
                              OfficialRepository officialRepository,
                              UserDetailsServiceImpl userDetailsService,
                              AccusationProcessRepository accusationProcessRepository,
                              BibleRepository bibleRepository,
                              LocalityRepository localityRepository,
                              PersonRepository personRepository) {
        this.inquisitionProcessRepository = inquisitionProcessRepository;
        this.accusationRecordRepository = accusationRecordRepository;
        this.officialRepository = officialRepository;
        this.userDetailsService = userDetailsService;
        this.accusationProcessRepository = accusationProcessRepository;
        this.bibleRepository = bibleRepository;
        this.localityRepository = localityRepository;
        this.personRepository = personRepository;
    }

    public Payload startProcess(InquisitionProcessStartContainer container) {
        User user = userDetailsService.getUser();
        Official official = officialRepository.getCurrentByPersonId(user.getPersonId());
        Bible bible = bibleRepository.find(container.getBibleId());
        Locality locality = localityRepository.find(container.getLocalityId());

        if (bible == null) {
            return new BasePayload(400, "Такой Библии не существует");
        }
        if (locality == null) {
            return new BasePayload(400, "Такой местности не существует");
        }

        StartInquisitionProcessRepContainer repContainer = new StartInquisitionProcessRepContainer(
                official.getId(),
                container.getLocalityId(),
                container.getBibleId()
        );

        QueryFetchHelper<StartInquisitionProcessRepContainer, Integer> helper = new QueryFetchHelper<>(
                repContainer,
                c -> inquisitionProcessRepository.startInquisitionProcess(
                        c.official(),
                        c.locality(),
                        c.bible()
                )
        );

        Integer processId = helper.fetch();
        if (processId == null) {
            return new BasePayload(400, INCORRECT_REQUEST);
        }
        return new PayloadWithData<>(200, processId);
    }

    public Payload getCurrentInquisitionProcessForInquisitor(Integer officialId) {
        QueryFetchHelper<Integer, List<InquisitionProcess>> helper = new QueryFetchHelper<>(
                officialId,
                inquisitionProcessRepository::findInProgressByOfficialId
        );

        return getCurrentInquisitionProcess(helper);
    }

    public Payload getCurrentInquisitionProcessForBishop() {
        User user = userDetailsService.getUser();
        Official official = officialRepository.getCurrentByPersonId(user.getPersonId());
        Person person = personRepository.find(official.getPersonId());

        Church church = churchRepository.findByLocality(person.getLocalityId());

        QueryFetchHelper<Integer, List<InquisitionProcess>> helper = new QueryFetchHelper<>(
                church.getId(),
                inquisitionProcessRepository::findInProgressByChurchId
        );

        return getCurrentInquisitionProcess(helper);
    }

    private Payload getCurrentInquisitionProcess(QueryFetchHelper<Integer, List<InquisitionProcess>> helper) {
        List<InquisitionProcess> processes = helper.fetch();
        if (processes == null || processes.isEmpty()) {
            CurrentInquisitionProcessPayload process = new CurrentInquisitionProcessPayload();
            process.setId(0);
            process.setCurrentAccusationProcess(0);
            return new PayloadWithData<>(200, process);
        }

        InquisitionProcess process = processes.get(0);
        int step = defineStepOfProcess(process);
        var payload = convertToCurrentPayload(process, step);

        return new PayloadWithData<>(200, payload);
    }

    private int defineStepOfProcess(InquisitionProcess process) {
        int step;
        AccusationProcess accusationProcess = process.getAccusationProcess();
        if (isFullNull(accusationProcess)) {
            step = 0;
        } else if (accusationProcess.getFinishTime() == null) {
            step = 1;
        } else if (!accusationRecordRepository.getNotResolvedAccusationRecord(accusationProcess.getId()).isEmpty()) {
            step = 2;
        } else if (process.getFinishData() == null) {
            step = 3;
        } else {
            step = 4;
        }
        return step;
    }

    public Payload getAllInquisitionProcess() {
        QueryFetchHelper<Void, List<InquisitionProcess>> helperInqRep = new QueryFetchHelper<>(
                null,
                id -> inquisitionProcessRepository.findAll()
        );
        List<InquisitionProcess> processes = helperInqRep.fetch();
        List<Integer> accusationProcessIds = processes.stream().map(k -> k.getAccusationProcess().getId()).toList();

        QueryFetchHelper<List<Integer>, List<Pair<Integer, Integer>>> helperAccRep = new QueryFetchHelper<>(
                accusationProcessIds,
                accusationRecordRepository::findAllCases
        );
        List<Pair<Integer, Integer>> accusationProcessToCountCases = helperAccRep.fetch();
        Map<Integer, List<InquisitionProcess>> processById = processes.stream()
                .collect(Collectors.groupingBy(InquisitionProcess::getId));

        List<InquisitionProcessPayload> payload = processById.values().stream()
                .map(
                        value -> {
                            var accusationIds = value.stream()
                                    .map(r -> r.getAccusationProcess().getId()).collect(Collectors.toSet());

                            var caseCount = accusationProcessToCountCases.stream()
                                    .filter(pair -> accusationIds.contains(pair.getLeft()))
                                    .map(Pair::getRight)
                                    .reduce(0, Integer::sum);

                            return InquisitionProcessConverter.convertToPayload(value.get(0), caseCount);
                        }
                )
                .toList();
        return new PayloadWithCollection<>(200, "", payload);
    }

    public Payload getAllCases(Integer accusationId) {
        AccusationProcess process = accusationProcessRepository.find(accusationId);
        if (process == null) {
            return new BasePayload(400, "Не найден процесс сбора доносов с id " + accusationId);
        }
        QueryFetchHelper<Integer, List<AccusationRecordFullWithCaseId>> helper = new QueryFetchHelper<>(
                accusationId,
                accusationRecordRepository::getNotResolvedAccusationRecordWithCases
        );
        List<AccusationRecordFullWithCaseId> records = helper.fetch();
        Map<Integer, List<AccusationRecordFullWithCaseId>> recordsByCaseId =
                records.stream().collect(Collectors.groupingBy(AccusationRecordFullWithCaseId::getCaseId));
        Map<Integer, AccusationRecordWithCasePayload> payloadMap = recordsByCaseId.entrySet().stream()
                .map(entry -> {
                            var key = entry.getKey();
                            var value = entry.getValue();

                            AccusationRecordWithCasePayload payload = new AccusationRecordWithCasePayload();
                            AccusationRecordFullWithCaseId first = value.get(0);

                            payload.setId(key);
                            payload.setAccused(first.getAccused().getName() + " " + first.getAccused().getSurname());
                            payload.setCreationDate(first.getCreationDate());
                            payload.setDescription(value.stream().map(AccusationRecordFull::getDescription).collect(Collectors.joining(", ")));
                            return new AbstractMap.SimpleEntry<>(key, payload);
                        }
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

//        List<Integer> caseIds = payloadMap.keySet().stream().toList();
//        Map<Integer, List<CaseLog>> caseLogs = investigativeCaseRepository.getCaseLogs(caseIds).stream().collect
//        (Collectors.groupingBy(CaseLog::getCaseId));
        return new PayloadWithCollection<>(200, payloadMap.values());
    }

    private boolean isFullNull(AccusationProcess process) {
        return process == null ||
                (process.getId() == null && process.getInquisitionProcessId() == null &&
                        process.getStartTime() == null && process.getFinishTime() == null);
    }

    public Payload finishProcess(InquisitionProcessIdContainer container) {
        QueryFetchHelper<Integer, Integer> helper = new QueryFetchHelper<>(
                container.getInquisitionId(),
                inquisitionProcessRepository::finishInquisitionProcess
        );

        Integer processId = helper.fetch();
        if (processId == null) {
            return new BasePayload(400, INCORRECT_REQUEST);
        }
        return new PayloadWithData<>(200, processId);
    }
}
