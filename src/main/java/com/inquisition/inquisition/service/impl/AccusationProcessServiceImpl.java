package com.inquisition.inquisition.service.impl;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.accusation.AccusationProcessWithIdContainer;
import com.inquisition.inquisition.model.accusation.AccusationProcessWithInqProcessId;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithInteger;
import com.inquisition.inquisition.repository.AccusationProcessRepository;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AccusationProcessServiceImpl {
    private final AccusationProcessRepository accusationProcessRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccusationProcessServiceImpl.class);

    public AccusationProcessServiceImpl(AccusationProcessRepository accusationProcessRepository) {
        this.accusationProcessRepository = accusationProcessRepository;
    }

    public Payload startProcess(AccusationProcessWithInqProcessId container) {
        try {
            Integer processId = accusationProcessRepository.startAccusationProcess(container.getInquisitionProcessId());
            return new PayloadWithInteger(200, "Accusation process was started.", processId);

        } catch (DataIntegrityViolationException | DataAccessException e) {
            logger.debug("Incorrect request AccusationProcessService:startProcess: {}", container);
            return new BasePayload(400, "Incorrect request");
        }
    }

    public Payload finishProcess(AccusationProcessWithIdContainer container) {
        AccusationProcess process = accusationProcessRepository.find(container.getAccusationId());
        if (process == null) {
            return new BasePayload(404, "Accusation process not found.");
        }

        try {
            accusationProcessRepository.finishAccusationProcess(container.getAccusationId());
            return new BasePayload(200, "Accusation process was finished.");

        } catch (DataIntegrityViolationException | DataAccessException e) {
            logger.debug("Incorrect request AccusationProcessService:finishProcess: {}", container);
            return new BasePayload(400, "Incorrect request. Accusation process have been already finished");
        }
    }

    public Payload generateCases(AccusationProcessWithIdContainer container) {
        AccusationProcess process = accusationProcessRepository.find(container.getAccusationId());
        if (process == null) {
            return new BasePayload(404, "Accusation process not found.");
        }

        try {
            accusationProcessRepository.generateCases(container.getAccusationId());
            return new BasePayload(200, "Generated cases.");

        } catch (DataIntegrityViolationException | DataAccessException e) {
            logger.debug("Incorrect request AccusationProcessService:generateCases: {}", container);
            return new BasePayload(400, "Incorrect request. Cases have been already generated");
        }
    }
}
