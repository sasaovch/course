package com.inquisition.inquisition.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordPayload;
import com.inquisition.inquisition.model.accusation.AccusationRecordWithCasePayload;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessPayload;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.payload.PayloadWithData;
import com.inquisition.inquisition.model.payload.PayloadWithInteger;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.InquisitionProcessRepository;
import com.inquisition.inquisition.repository.UserRepository;
import com.inquisition.inquisition.security.UserDetailsServiceImpl;
import com.inquisition.inquisition.service.intr.InquisitionService;
import com.inquisition.inquisition.utils.AccusationRecordConverter;
import com.inquisition.inquisition.utils.InquisitionProcessConverter;
import com.nimbusds.jose.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.utils.InquisitionProcessConverter.convertToCurrentPayload;

@Service
public class InquisitionServiceImpl implements InquisitionService {
    @Autowired
    InquisitionProcessRepository inquisitionProcessRepository;
    @Autowired
    AccusationRecordRepository accusationRecordRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    public Payload startProcess(InquisitionProcessStartContainer container) {
        User user = userDetailsService.getUser();
        if (user == null) {
            return new BasePayload(400, "User not found.");
        }

        Integer processId = inquisitionProcessRepository.startInquisitionProcess(
                user.getPersonId(),
                container.getLocalityId(),
                container.getBibleId()
        );
        return new PayloadWithInteger(200, "Inquisition process was created.", processId);
    }

    @Override
    public Payload getCurrentInquisitionProcess(Integer officialId) {
        User user = userDetailsService.getUser();
        if (user == null) {
            return new BasePayload(400, "User not found.");
        }
        //FIXME: officialId == null???

        List<InquisitionProcess> processes = inquisitionProcessRepository.findInProgressByOfficialId(officialId);
        if (processes.isEmpty()) {//FIXME: check this
            return new BasePayload(400, "Process not found.");
        }
        //FIXME: старт процесса сбора доносов и весь пайплайн
        InquisitionProcess process = processes.get(0);
        int step = defineStepOfProcess(process);

        var payload = convertToCurrentPayload(process, step);
        return new PayloadWithData<>(200, "", payload);
    }

    private int defineStepOfProcess(InquisitionProcess process) {
        int step;
        AccusationProcess accusationProcess = process.getAccusationProcess();

        if (accusationProcess == null) {
            step = 0;
        } else if (accusationProcess.getFinishTime() == null) {
            step = 1;
        } else if (
                !accusationRecordRepository.getNotResolvedAccusationRecord(
                        accusationProcess.getId()
                ).isEmpty()
        ) {
            step = 2;
        } else if (process.getFinishDate() == null) {
            step = 3;
        } else {
            step = 4;
        }
        return step;
    }

    @Override
    public Payload getAllInquisitionProcess() {
        List<InquisitionProcess> processes = inquisitionProcessRepository.findAll();

//        Map<Integer, Integer> accusationProcessToInquisition = processes.stream()
//                .collect(Collectors.toMap(k -> k.getAccusationProcess().getId(), InquisitionProcess::getId));
        List<Integer> accusationProcessIds = processes.stream().map(k -> k.getAccusationProcess().getId()).toList();
//                accusationProcessToInquisition.keySet().stream().toList();

        List<Pair<Integer, Integer>> accusationProcessToCountCases =
                accusationRecordRepository.findAllCases(accusationProcessIds);
        Map<Integer, List<InquisitionProcess>> processById = processes.stream()
                .collect(Collectors.groupingBy(InquisitionProcess::getId));

//        Map<Integer, Long> processIdToCaseCount = processes.stream()
//                .collect(Collectors.groupingBy(InquisitionProcess::getId, Collectors.counting()));

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

//    public List<AccusationRecordWithCasePayload> getQueueForDiscussion(Integer process) {
//
//    }

//    @Override
//    //FIXME: нужно написать конвертеры в ожидаемые объекты
//    public Payload getAllAccusationRecords(Integer accusationProcessId) {
//        if (accusationProcessId == null) {
//            return new BasePayload(200, "Nothing was found");
//        }
//        List<AccusationRecordFull> records = accusationRecordRepository.findByProcessId(accusationProcessId);
//        List<AccusationRecordPayload> payload =
//                records.stream().map(AccusationRecordConverter::convertToPayload).toList();
//        return new PayloadWithCollection<>(200, "", payload);
//    }
}
