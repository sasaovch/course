package com.inquisition.inquisition.service;

import java.util.List;
import java.util.function.Function;

import com.inquisition.inquisition.Pair;
import com.inquisition.inquisition.model.accusation.container.AccusationProcessWithIdContainer;
import com.inquisition.inquisition.model.accusation.container.AccusationProcessWithInqProcessId;
import com.inquisition.inquisition.model.accusation.container.AddAccusationRecordContainer;
import com.inquisition.inquisition.model.accusation.entity.AccusationProcess;
import com.inquisition.inquisition.model.accusation.entity.AccusationRecordComplex;
import com.inquisition.inquisition.model.accusation.payload.AccusationRecordPayload;
import com.inquisition.inquisition.model.commandment.container.ConnectCommandmentContainer;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.payload.PayloadWithInteger;
import com.inquisition.inquisition.repository.AccusationProcessRepository;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.helper.QueryFetchHelper;
import com.inquisition.inquisition.utils.AccusationRecordConverter;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.utils.Messages.DONE;
import static com.inquisition.inquisition.utils.Messages.ERROR_WHILE_HANDLE_REQUEST;

@Service
public class AccusationProcessService {
    private final AccusationRecordRepository accusationRecordRepository;
    private final AccusationProcessRepository accusationProcessRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccusationProcessService.class);

    public AccusationProcessService(AccusationRecordRepository accusationRecordRepository,
                                    AccusationProcessRepository accusationProcessRepository) {
        this.accusationRecordRepository = accusationRecordRepository;
        this.accusationProcessRepository = accusationProcessRepository;
    }

    public Payload startProcess(AccusationProcessWithInqProcessId container) {
        AccusationProcess process =
                accusationProcessRepository.findByInquisitionProcess(container.getInquisitionProcessId());
        if (process != null) {
            return new BasePayload(400, "Процесс сбора доносов уже начат");
        }

        QueryFetchHelper<Integer, Integer> helper = new QueryFetchHelper<>(
                container.getInquisitionProcessId(), accusationProcessRepository::startAccusationProcess
        );
        Pair<Integer, String> pair = helper.fetch();
        if (pair.getFirst() == null) {
            return new BasePayload(400, pair.getSecond());
        }
        return new PayloadWithInteger(200, "Начался процесс сбора доносов", pair.getFirst());
    }

    public Payload finishProcess(AccusationProcessWithIdContainer container) {
        AccusationProcess process = accusationProcessRepository.find(container.getAccusationId());
        if (process == null) {
            return new BasePayload(404, "Процесс сбора доносов не найден.");
        }

        QueryFetchHelper<Integer, Void> helper = new QueryFetchHelper<>(
                container.getAccusationId(), accusationProcessRepository::finishAccusationProcess
        );
        Pair<Void, String> pair = helper.fetch();
        if (pair.getSecond() != null) {
            return new BasePayload(400, pair.getSecond());
        }
        return new BasePayload(200, "Процесс сбора доносов закончен.");
    }

    public Payload generateCases(AccusationProcessWithIdContainer container) {
        AccusationProcess process = accusationProcessRepository.find(container.getAccusationId());
        if (process == null) {
            return new BasePayload(404, "Процесс сбора доносов не найден.");
        }

        try {
            accusationProcessRepository.generateCases(container.getAccusationId());
            accusationProcessRepository.handleSimpleCases(container.getAccusationId());
            accusationProcessRepository.handleCasesWithGraveSin(container.getAccusationId());
            return new BasePayload(200, DONE);

        } catch (DataIntegrityViolationException | DataAccessException e) {
            logger.debug("Incorrect request AccusationProcessService:generateCases: {}", container);
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }
    }

    public Payload getNotResolvedAccusationRecord(Integer accusationId) {
        return getAccusationProcessWrapper(
                accusationId,
                accusationRecordRepository::getNotResolvedAccusationRecordComplex
        );
    }

    public Payload getAllAccusationRecords(Integer accusationId) {
        return getAccusationProcessWrapper(
                accusationId,
                accusationRecordRepository::findByProcessId
        );
    }

    private Payload getAccusationProcessWrapper(Integer accusationId,
                                                Function<Integer, List<AccusationRecordComplex>> sqlRequest) {
        if (accusationId == null) {
            return new BasePayload(400, "Процесс id не может быть null");
        }
        AccusationProcess process = accusationProcessRepository.find(accusationId);
        if (process == null) {
            return new BasePayload(400, "Процесс с id " + accusationId + " не найден");
        }
        QueryFetchHelper<Integer, List<AccusationRecordComplex>> helper = new QueryFetchHelper<>(
                accusationId,
                sqlRequest
        );
        Pair<List<AccusationRecordComplex>, String> pair = helper.fetch();
        List<AccusationRecordComplex> records = pair.getFirst();
        if (records == null) {
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }
        List<AccusationRecordPayload> payload =
                records.stream().map(AccusationRecordConverter::convertToPayload).toList();
        return new PayloadWithCollection<>(200, payload);
    }

    public Payload addRecord(AddAccusationRecordContainer container) {
        AccusationProcess process = accusationProcessRepository.find(container.getAccusationId());
        if (process == null) {
            return new BasePayload(400, "Не найден процесс с таким id " + container.getAccusationId());
        }
        try {
            accusationRecordRepository.addRecord(
                    container.getAccusationId(),
                    container.getAccused(),
                    container.getBishop(),
                    container.getDateTime(),
                    container.getDescription(),
                    container.getInformer(),
                    container.getViolationPlace()
            );
        } catch (DataAccessException e) {
            return new BasePayload(400, "Процесс сбора доносов завершен");
        } catch (DataIntegrityViolationException e) {
            return new BasePayload(400, "Некорректные данные");
        }
        return new BasePayload(200, DONE);
    }

    public Payload connectCommandment(ConnectCommandmentContainer container, Integer recordId) {
        container.getCommandments().forEach(commandment ->
                accusationRecordRepository.connectCommandment(commandment, recordId)
        );
        return new BasePayload(200, DONE);
    }
}
