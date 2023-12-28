package com.inquisition.inquisition.service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.inquisition.inquisition.Pair;
import com.inquisition.inquisition.model.accusation.entity.AccusationInvestigativeCase;
import com.inquisition.inquisition.model.accusation.entity.AccusationProcess;
import com.inquisition.inquisition.model.accusation.entity.AccusationRecordComplex;
import com.inquisition.inquisition.model.cases.payload.CaseLogPayload;
import com.inquisition.inquisition.model.bible.entity.Bible;
import com.inquisition.inquisition.model.cases.entity.CaseLog;
import com.inquisition.inquisition.model.cases.entity.InquisitionCaseLog;
import com.inquisition.inquisition.model.church.entity.Church;
import com.inquisition.inquisition.model.inquisition.payload.CurrentInquisitionProcessPayload;
import com.inquisition.inquisition.model.inquisition.entity.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.container.InquisitionProcessIdContainer;
import com.inquisition.inquisition.model.inquisition.payload.InquisitionProcessPayload;
import com.inquisition.inquisition.model.inquisition.container.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.inquisition.container.StartInquisitionProcessRepContainer;
import com.inquisition.inquisition.model.investigativecase.entity.CaseViolation;
import com.inquisition.inquisition.model.investigativecase.entity.InvestigativeCase;
import com.inquisition.inquisition.model.locality.entity.Locality;
import com.inquisition.inquisition.model.official.entity.Official;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.payload.PayloadWithData;
import com.inquisition.inquisition.model.person.entity.Person;
import com.inquisition.inquisition.model.user.entity.User;
import com.inquisition.inquisition.models.enums.CaseLogResult;
import com.inquisition.inquisition.models.enums.CaseLogStatus;
import com.inquisition.inquisition.repository.AccusationProcessRepository;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.BibleRepository;
import com.inquisition.inquisition.repository.ChurchRepository;
import com.inquisition.inquisition.repository.InquisitionProcessRepository;
import com.inquisition.inquisition.repository.InvestigativeCaseRepository;
import com.inquisition.inquisition.repository.LocalityRepository;
import com.inquisition.inquisition.repository.OfficialRepository;
import com.inquisition.inquisition.repository.PersonRepository;
import com.inquisition.inquisition.repository.helper.QueryFetchHelper;
import com.inquisition.inquisition.security.UserDetailsServiceImpl;
import com.inquisition.inquisition.utils.InquisitionProcessConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.utils.InquisitionProcessConverter.convertToCurrentPayload;
import static com.inquisition.inquisition.utils.Messages.INCORRECT_REQUEST;

@Service
public class InquisitionProcessService {
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
    @Autowired
    private InvestigativeCaseRepository investigativeCaseRepository;

    public InquisitionProcessService(InquisitionProcessRepository inquisitionProcessRepository,
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

        Pair<Integer, String> pair = helper.fetch();
        Integer processId = pair.getFirst();
        String messageRes = pair.getSecond();
        if (processId == null) {
            if (messageRes != null) return new BasePayload(400, messageRes);
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
        Person person = personRepository.find(user.getPersonId());

        Church church = churchRepository.findByLocality(person.getLocalityId());

        QueryFetchHelper<Integer, List<InquisitionProcess>> helper = new QueryFetchHelper<>(
                church.getId(),
                inquisitionProcessRepository::findInProgressByChurchId
        );

        return getCurrentInquisitionProcess(helper);
    }

    private Payload getCurrentInquisitionProcess(QueryFetchHelper<Integer, List<InquisitionProcess>> helper) {
        List<InquisitionProcess> processes = helper.fetch().getFirst();
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

    public Payload getAllInquisitionProcess() {
        QueryFetchHelper<Void, List<InquisitionProcess>> helperInqRep = new QueryFetchHelper<>(
                null,
                id -> inquisitionProcessRepository.findAll()
        );
        Pair<List<InquisitionProcess>, String> result = helperInqRep.fetch();
        List<InquisitionProcess> processes = result.getFirst();
        if (processes == null) {
           return new BasePayload(400, result.getSecond());
        }

        List<Integer> accusationProcessIds = processes.stream().map(k -> k.getAccusationProcess().getId()).toList();
        Map<Integer, List<InquisitionProcess>> processById = processes.stream()
                .collect(Collectors.groupingBy(InquisitionProcess::getId));

        QueryFetchHelper<List<Integer>, List<Pair<Integer, Integer>>> helperAccRep = new QueryFetchHelper<>(
                accusationProcessIds,
                accusationRecordRepository::findAllCases
        );

        Pair<List<Pair<Integer, Integer>>, String> resultAccRep = helperAccRep.fetch();
        List<Pair<Integer, Integer>> accusationProcessToCountCases = resultAccRep.getFirst();
        if (accusationProcessToCountCases == null) {
            return new BasePayload(400, resultAccRep.getSecond());
        }

        List<InquisitionProcessPayload> payload = processById.values().stream()
                .map(
                        value -> {
                            var accusationIds = value.stream()
                                    .map(r -> r.getAccusationProcess().getId()).collect(Collectors.toSet());

                            var caseCount = accusationProcessToCountCases.stream()
                                    .filter(pair -> accusationIds.contains(pair.getFirst()))
                                    .map(Pair::getSecond)
                                    .reduce(0, Integer::sum);

                            return InquisitionProcessConverter.convertToPayload(value.get(0), caseCount);
                        }
                )
                .toList();
        return new PayloadWithCollection<>(200, payload);
    }

    public Payload getAllCases(Integer inquisitionProcessId) {
        QueryFetchHelper<Integer, List<AccusationInvestigativeCase>> helperNotResolvedCases = new QueryFetchHelper<>(
                inquisitionProcessId,
                investigativeCaseRepository::getNotResolvedCasesIdWithAccusationRecordId
        );
        //FIXME: посмотреть что будет если ничего не нашлось
        Pair<List<AccusationInvestigativeCase>, String> pair = helperNotResolvedCases.fetch();
        List<AccusationInvestigativeCase> records = pair.getFirst();
        if (records == null) {
            return new BasePayload(400, pair.getSecond());
        }

        Map<Integer, List<AccusationInvestigativeCase>> recordsByCaseId =
                records.stream().collect(Collectors.groupingBy(AccusationInvestigativeCase::getCaseId));

        List<Integer> caseIds = recordsByCaseId.keySet().stream().toList();
        List<Integer> recordIds = recordsByCaseId.values().stream().flatMap(List::stream).map(AccusationInvestigativeCase::getRecordId).toList();
        QueryFetchHelper<List<Integer>, List<InquisitionCaseLog>> helper = new QueryFetchHelper<>(
                caseIds,
                inquisitionProcessRepository::getCases
        );
        Pair<List<InquisitionCaseLog>, String> resultCaseLogs = helper.fetch();
        List<InquisitionCaseLog> caseLogs = resultCaseLogs.getFirst();
        if (caseLogs == null) {
            return new BasePayload(400, pair.getSecond());
        }
        Map<Integer, List<InquisitionCaseLog>> caseLogByCaseId = caseLogs.stream()
                .collect(Collectors.groupingBy(caseLog -> caseLog.getInvestigativeCase().getId()));

        Map<Integer, List<CaseViolation>> violationsByCaseId = caseIds.stream()
                .collect(Collectors.toMap(Function.identity(), investigativeCaseRepository::getCaseViolations));

        Map<Integer, AccusationRecordComplex> recordsMap = accusationRecordRepository.findById(recordIds).stream().collect(Collectors.toMap(AccusationRecordComplex::getId, Function.identity()));
        Map<Integer, CaseLogPayload> payloadMap = caseLogByCaseId.entrySet().stream()
                .map(entry -> {
                            Integer key = entry.getKey();
                            List<InquisitionCaseLog> value = entry.getValue(); // будет много изза нескольких caseLog
                            Set<Integer> recordIdsList = recordsByCaseId.get(key).stream().map(AccusationInvestigativeCase::getRecordId).collect(Collectors.toSet());
                            List<CaseLog> logs = value.stream().map(InquisitionCaseLog::getCaseLog).toList();
                            List<CaseViolation> violations = violationsByCaseId.get(key);

                            InvestigativeCase caseLog = value.get(0).getInvestigativeCase();
                            AccusationRecordComplex record = recordsMap.get(value.get(0).getAccusationRecord().getId());

                            Optional<CaseLog> conversatoin = value.stream().filter(c -> c.getCaseLog().getStatus() == CaseLogStatus.Conversation).map(InquisitionCaseLog::getCaseLog).findFirst();
                            Optional<CaseLog> torture = value.stream().filter(c -> c.getCaseLog().getStatus() == CaseLogStatus.Torture).map(InquisitionCaseLog::getCaseLog).findFirst();
                            Optional<CaseLog> punishment = value.stream().filter(c -> c.getCaseLog().getStatus() == CaseLogStatus.Punishment).map(InquisitionCaseLog::getCaseLog).findFirst();

                            Pair<Integer, String> status = getCaseStatus(conversatoin, torture, punishment);

                            CaseLogPayload payload = new CaseLogPayload();
                            payload.setId(key);
                            payload.setAccused(record.getAccused().getName() + " " + record.getAccused().getSurname());
                            payload.setCreationDate(caseLog.getCreationDate());
                            payload.setDescription(recordsMap.values().stream().filter(r -> recordIdsList.contains(r.getId())).map(AccusationRecordComplex::getDescription).collect(Collectors.joining(", ")));
                            payload.setStatus(status.getSecond());
                            payload.setViolationDescription(violations.stream().map(c -> c.getCommandment().getDescription()).collect(Collectors.joining(", ")));
                            payload.setStep(status.getFirst());
                            return new AbstractMap.SimpleEntry<>(key, payload);
                        }
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new PayloadWithCollection<>(200, payloadMap.values());
    }

    private Pair<Integer, String> getCaseStatus(Optional<CaseLog> conversation, Optional<CaseLog> torture, Optional<CaseLog> punishment) {
        if (conversation.isEmpty()) {
            return Pair.of(0, "обработка не начата");
        }
        CaseLog c = conversation.get();
        if (c.getFinishTime() == null) {
            return Pair.of(1, "идет беседа");
        }
        if (torture.isEmpty()) {
            var result = c.getResult() == CaseLogResult.Admission ? "признался" : "не признался";
            return Pair.of(2, "беседа окончена " + result);
        }
        CaseLog t = torture.get();
        if (t.getFinishTime() == null) {
            return Pair.of(3, "идут пытки");
        }
        if (punishment.isEmpty()) {
            var result = t.getResult() == CaseLogResult.Admission ? "признался" : "не признался";
            return Pair.of(4, "пытка окончена " + result);
        }
        return Pair.of(5, "отправлен на наказание");
    }

    public Payload finishProcess(InquisitionProcessIdContainer container) {
        QueryFetchHelper<Integer, Integer> helper = new QueryFetchHelper<>(
                container.getInquisitionId(),
                inquisitionProcessRepository::finishInquisitionProcess
        );

        Pair<Integer, String> result = helper.fetch();
        Integer processId = result.getFirst();
        if (processId == null) {
            return new BasePayload(400, result.getSecond());
        }
        return new PayloadWithData<>(200, processId);
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


    private boolean isFullNull(AccusationProcess process) {
        return process == null ||
                (process.getId() == null && process.getInquisitionProcessId() == null &&
                        process.getStartTime() == null && process.getFinishTime() == null);
    }
}
