package com.inquisition.inquisition.service.impl;

import com.inquisition.inquisition.model.cases.CaseInput;
import com.inquisition.inquisition.model.cases.CaseWithResultInput;
import com.inquisition.inquisition.model.cases.CaseWithStepInput;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessIdContainer;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.repository.CaseLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseLogService {
    @Autowired
    CaseLogRepository caseLogRepository;
    public Payload sendToDiscussion(CaseInput input) {
        caseLogRepository.sendToDiscussion(input.getId());
        return new BasePayload(200, "Sent");
    }

    public Payload finishProcess(CaseWithResultInput input) {
        caseLogRepository.finishProcess(input.getId(), input.getResult(), input.getDescription());
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

    public Payload getCasesForDiscussion(InquisitionProcessIdContainer input) {
        caseLogRepository.getCasesForDiscussion(input.getInquisitionId());
        return new BasePayload(200, "Made");
    }
}
