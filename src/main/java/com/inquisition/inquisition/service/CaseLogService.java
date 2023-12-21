package com.inquisition.inquisition.service;

import java.util.List;
import java.util.function.Function;

import com.inquisition.inquisition.model.cases.CaseInput;
import com.inquisition.inquisition.model.cases.CaseWithResultInput;
import com.inquisition.inquisition.model.cases.CaseWithStepInput;
import com.inquisition.inquisition.model.cases.InquisitionCaseLog;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.models.enums.CaseLogStatus;
import com.inquisition.inquisition.repository.CaseLogRepository;
import com.inquisition.inquisition.repository.InquisitionProcessRepository;
import com.inquisition.inquisition.repository.QueryFetchHelper;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.utils.Messages.ERROR_WHILE_HANDLE_REQUEST;

@Service
public class CaseLogService {
    private final CaseLogRepository caseLogRepository;
    private final InquisitionProcessRepository inquisitionProcessRepository;

    public CaseLogService(CaseLogRepository caseLogRepository,
                          InquisitionProcessRepository inquisitionProcessRepository) {
        this.caseLogRepository = caseLogRepository;
        this.inquisitionProcessRepository = inquisitionProcessRepository;
    }

    public Payload sendToDiscussion(CaseInput input) {
        caseLogRepository.sendToDiscussion(input.getId());
        return new BasePayload(200, "Sent");
    }

    public Payload finishDiscussion(CaseWithResultInput input) {
        caseLogRepository.finishProcess(input.getId(), input.getResult(), input.getDescription(),
                CaseLogStatus.Conversation);
        return new BasePayload(200, "Finished");
    }

    public Payload finishTorture(CaseWithResultInput input) {
        caseLogRepository.finishProcess(input.getId(), input.getResult(), input.getDescription(),
                CaseLogStatus.Torture);
        return new BasePayload(200, "Finished");
    }

    public Payload sendToTorture(CaseInput input) {
        caseLogRepository.sendToTorture(input.getId());
        return new BasePayload(200, "Sent");
    }

    public Payload makeTortureStep(CaseWithStepInput input) {
        caseLogRepository.makeTortureStep(input.getId(), input.getStep());
        return new BasePayload(200, "Made");
    }

    public Payload getCasesForDiscussion(Integer inquisitionId) {
        return getCasesFor(inquisitionId, inquisitionProcessRepository::getCasesForDiscussion);
    }

    public Payload getCasesForTorture(Integer inquisitionId) {
        return getCasesFor(inquisitionId, inquisitionProcessRepository::getCasesForTorture);
    }

    public Payload getCasesForPunishment(Integer inquisitionId) {
        return getCasesFor(inquisitionId, inquisitionProcessRepository::getCasesForPunishment);
    }

    private Payload getCasesFor(Integer inquisitionId, Function<Integer, List<InquisitionCaseLog>> sqlRequest) {
        QueryFetchHelper<Integer, List<InquisitionCaseLog>> helper = new QueryFetchHelper<>(
                inquisitionId,
                sqlRequest
        );
        List<InquisitionCaseLog> caseLogs = helper.fetch();
        if (caseLogs == null) {
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }
        return new PayloadWithCollection<>(200, caseLogs);
    }
}
