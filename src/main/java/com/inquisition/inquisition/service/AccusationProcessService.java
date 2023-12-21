package com.inquisition.inquisition.service;

import java.util.List;
import java.util.function.Function;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.accusation.AccusationProcessWithIdContainer;
import com.inquisition.inquisition.model.accusation.AccusationProcessWithInqProcessId;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordPayload;
import com.inquisition.inquisition.model.accusation.AddAccusationRecordContainer;
import com.inquisition.inquisition.model.accusation.ConnectCommandmentContainer;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.payload.PayloadWithInteger;
import com.inquisition.inquisition.repository.AccusationProcessRepository;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.QueryFetchHelper;
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
        try {
            //FIXME
            // : по хорошему сделать валидацию, что нельзя начать дважды процесс
            Integer processId = accusationProcessRepository.startAccusationProcess(container.getInquisitionProcessId());
            return new PayloadWithInteger(200, "Процесс сбора доносов начат.", processId);

        } catch (DataIntegrityViolationException | DataAccessException e) {
            logger.debug("Incorrect request AccusationProcessService:startProcess: {}", container);
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }
    }

    public Payload finishProcess(AccusationProcessWithIdContainer container) {
        AccusationProcess process = accusationProcessRepository.find(container.getAccusationId());
        if (process == null) {
            return new BasePayload(404, "Такой процесс сбора доносов не найден.");
        }

        try {
            accusationProcessRepository.finishAccusationProcess(container.getAccusationId());
            return new BasePayload(200, "Процесс сбора доносов закончен.");

        } catch (DataIntegrityViolationException | DataAccessException e) {
            logger.debug("Incorrect request AccusationProcessService:finishProcess: {}", container);
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }
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
                accusationRecordRepository::getNotResolvedAccusationRecordWithSubFields
        );
    }

    public Payload getAllAccusationRecords(Integer accusationId) {
        return getAccusationProcessWrapper(
                accusationId,
                accusationRecordRepository::findByProcessId
        );
    }

    private Payload getAccusationProcessWrapper(Integer accusationId,
                                                Function<Integer, List<AccusationRecordFull>> sqlRequest) {
        if (accusationId == null) {
            return new BasePayload(400, "Процесс id не может быть null");
        }
        AccusationProcess process = accusationProcessRepository.find(accusationId);
        if (process == null) {
            return new BasePayload(400, "Процесс с id " + accusationId + " не найден");
        }
        QueryFetchHelper<Integer, List<AccusationRecordFull>> helper = new QueryFetchHelper<>(
                accusationId,
                sqlRequest
        );
        List<AccusationRecordFull> records = helper.fetch();
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
