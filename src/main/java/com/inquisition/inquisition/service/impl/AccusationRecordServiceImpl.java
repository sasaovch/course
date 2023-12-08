package com.inquisition.inquisition.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordFullWithCaseId;
import com.inquisition.inquisition.model.accusation.AccusationRecordPayload;
import com.inquisition.inquisition.model.accusation.AccusationRecordWithCasePayload;
import com.inquisition.inquisition.model.accusation.AddAccusationRecordContainer;
import com.inquisition.inquisition.model.accusation.ConnectCommandmentContainer;
import com.inquisition.inquisition.model.cases.CaseLog;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.InvestigativeCaseRepository;
import com.inquisition.inquisition.utils.AccusationRecordConverter;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.KotlinDetector;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
public class AccusationRecordServiceImpl {
    @Autowired
    AccusationRecordRepository accusationRecordRepository;
    @Autowired
    InvestigativeCaseRepository investigativeCaseRepository;

    public Payload getNotResolvedAccusationRecord(Integer accusationId) {
        List<AccusationRecordFull> records =
                accusationRecordRepository.getNotResolvedAccusationRecordWithSubFields(accusationId);
        List<AccusationRecordPayload> payload =
                records.stream().map(AccusationRecordConverter::convertToPayload).toList();
        return new PayloadWithCollection<>(200, "", payload);
    }

    public Payload getAllCases(Integer accusationId) {
        List<AccusationRecordFullWithCaseId> records =
                accusationRecordRepository.getNotResolvedAccusationRecordWithCases(accusationId);
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

        List<Integer> caseIds = payloadMap.keySet().stream().toList();
        Map<Integer, List<CaseLog>> caseLogs = investigativeCaseRepository.getCaseLogs(caseIds).stream().collect(Collectors.groupingBy(CaseLog::getCaseId));
        return new PayloadWithCollection<>(200, "", payloadMap.values());
    }

    //FIXME: нужно написать конвертеры в ожидаемые объекты
    public Payload getAllAccusationRecords(Integer accusationProcessId) {
        if (accusationProcessId == null) {
            return new BasePayload(200, "Nothing was found");
        }
        List<AccusationRecordFull> records = accusationRecordRepository.findByProcessId(accusationProcessId);
        List<AccusationRecordPayload> payload = records.stream().map(AccusationRecordConverter::convertToPayload).toList();
        return new PayloadWithCollection<>(200, "", payload);
    }

    public Payload addRecord(AddAccusationRecordContainer container) {
        try {
            accusationRecordRepository.addRecord(
                    container.getAccusationId(),
                    container.getAccused(),
                    container.getBishop(),
                    container.getDateTime(),
                    container.getDescription(),
                    container.getInformer()
            );
        } catch (Exception e) {
            return new BasePayload(400, "Процесс сбора доносов уже окончен");
        }
        return new BasePayload(200, "Added");
    }

    public Payload connectCommandment(ConnectCommandmentContainer container, Integer recordId) {
        container.getCommandments().forEach(commandment -> {
            accusationRecordRepository.connectCommandment(commandment, recordId);
        });
        return new BasePayload(200, "Connected");
    }
}
