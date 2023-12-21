package com.inquisition.inquisition.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessPayload;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.inquisition.StartInquisitionProcessRepContainer;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.payload.PayloadWithData;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.InquisitionProcessRepository;
import com.inquisition.inquisition.repository.OfficialRepository;
import com.inquisition.inquisition.repository.QueryFetchHelper;
import com.inquisition.inquisition.security.UserDetailsServiceImpl;
import com.inquisition.inquisition.service.intr.InquisitionService;
import com.inquisition.inquisition.utils.InquisitionProcessConverter;
import com.nimbusds.jose.util.Pair;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.utils.InquisitionProcessConverter.convertToCurrentPayload;

@Service
public class InquisitionServiceImpl implements InquisitionService {
    private final InquisitionProcessRepository inquisitionProcessRepository;
    private final AccusationRecordRepository accusationRecordRepository;

    private final OfficialRepository officialRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public InquisitionServiceImpl(InquisitionProcessRepository inquisitionProcessRepository,
                                  AccusationRecordRepository accusationRecordRepository,
                                  OfficialRepository officialRepository,
                                  UserDetailsServiceImpl userDetailsService) {
        this.inquisitionProcessRepository = inquisitionProcessRepository;
        this.accusationRecordRepository = accusationRecordRepository;
        this.officialRepository = officialRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Payload startProcess(InquisitionProcessStartContainer container) {
        User user = userDetailsService.getUser();
        Official official = officialRepository.getCurrentByPersonId(user.getPersonId());
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
            return new BasePayload(400, "Incorrect request");
        }
        return new PayloadWithData<>(200, processId);
    }

    @Override
    public Payload getCurrentInquisitionProcess(Integer officialId) {
        QueryFetchHelper<Integer, List<InquisitionProcess>> helper = new QueryFetchHelper<>(
                officialId,
                inquisitionProcessRepository::findInProgressByOfficialId
        );

        List<InquisitionProcess> processes = helper.fetch();
        if (processes == null || processes.isEmpty()) {
            return new BasePayload(404, "Inquisition process not found.");
        }

        InquisitionProcess process = processes.get(0);
        int step = defineStepOfProcess(process);
        var payload = convertToCurrentPayload(process, step);

        return new PayloadWithData<>(200, payload);
    }

    private int defineStepOfProcess(InquisitionProcess process) {
        int step;
        AccusationProcess accusationProcess = process.getAccusationProcess();

        if (accusationProcess == null) {
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
}
