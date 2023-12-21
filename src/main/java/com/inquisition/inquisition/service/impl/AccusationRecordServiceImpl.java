package com.inquisition.inquisition.service.impl;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordFullWithCaseId;
import com.inquisition.inquisition.model.accusation.AccusationRecordPayload;
import com.inquisition.inquisition.model.accusation.AccusationRecordWithCasePayload;
import com.inquisition.inquisition.model.accusation.AddAccusationRecordContainer;
import com.inquisition.inquisition.model.accusation.ConnectCommandmentContainer;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.repository.AccusationProcessRepository;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.QueryFetchHelper;
import com.inquisition.inquisition.utils.AccusationRecordConverter;
import org.jooq.exception.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AccusationRecordServiceImpl {
    private final AccusationRecordRepository accusationRecordRepository;
    private final AccusationProcessRepository accusationProcessRepository;

    public AccusationRecordServiceImpl(AccusationRecordRepository accusationRecordRepository,
                                       AccusationProcessRepository accusationProcessRepository) {
        this.accusationRecordRepository = accusationRecordRepository;
        this.accusationProcessRepository = accusationProcessRepository;
    }

    public Payload getNotResolvedAccusationRecord(Integer accusationId) {
        try {
            return getAccusationProcessWrapper(
                    accusationId,
                    accusationRecordRepository::getNotResolvedAccusationRecordWithSubFields
            );
        } catch (DataAccessException e) {
            return new BasePayload(400, "Процесс сбора доносов еще не окончен");
        }
    }

    public Payload getAllAccusationRecords(Integer accusationId) {
        return getAccusationProcessWrapper(
                accusationId,
                accusationRecordRepository::findByProcessId
        );
    }

    public Payload getAllCases(Integer accusationId) {
        AccusationProcess process = accusationProcessRepository.find(accusationId);
        if (process == null) {
            return new BasePayload(400, "Not found accusation process with id " + accusationId);
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

        List<Integer> caseIds = payloadMap.keySet().stream().toList();
//        Map<Integer, List<CaseLog>> caseLogs = investigativeCaseRepository.getCaseLogs(caseIds).stream().collect
//        (Collectors.groupingBy(CaseLog::getCaseId));
        return new PayloadWithCollection<>(200, payloadMap.values());
    }

    private Payload getAccusationProcessWrapper(Integer accusationId,
                                                Function<Integer, List<AccusationRecordFull>> sqlRequest) {
        if (accusationId == null) {
            return new BasePayload(400, "Accusation process id cannot be null");
        }
        AccusationProcess process = accusationProcessRepository.find(accusationId);
        if (process == null) {
            return new BasePayload(400, "Not found accusation process with id " + accusationId);
        }
        QueryFetchHelper<Integer, List<AccusationRecordFull>> helper = new QueryFetchHelper<>(
                accusationId,
                sqlRequest
        );
        List<AccusationRecordFull> records = helper.fetch();
        List<AccusationRecordPayload> payload =
                records.stream().map(AccusationRecordConverter::convertToPayload).toList();
        return new PayloadWithCollection<>(200, payload);
    }

    public Payload addRecord(AddAccusationRecordContainer container) {
        AccusationProcess process = accusationProcessRepository.find(container.getAccusationId());
        if (process == null) {
            return new BasePayload(400, "Not found accusation process with id " + container.getAccusationId());
        }
        try {
            accusationRecordRepository.addRecord(
                    container.getAccusationId(),
                    container.getAccused(),
                    container.getBishop(),
                    container.getDateTime(),
                    container.getDescription(),
                    container.getInformer()
            );
        } catch (DataAccessException e) {
            return new BasePayload(400, "Process of collecting records has been finished");
        } catch (DataIntegrityViolationException e) {
            return new BasePayload(400, "Incorrect data");
        }
        return new BasePayload(200, "Added");
    }

    public Payload connectCommandment(ConnectCommandmentContainer container, Integer recordId) {
        container.getCommandments().forEach(commandment ->
                accusationRecordRepository.connectCommandment(commandment, recordId)
        );
        return new BasePayload(200, "Connected");
    }
}
