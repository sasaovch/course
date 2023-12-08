package com.inquisition.inquisition.service.impl;

import com.inquisition.inquisition.model.accusation.AccusationProcessWithIdContainer;
import com.inquisition.inquisition.model.accusation.AccusationProcessStartContainer;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithInteger;
import com.inquisition.inquisition.repository.AccusationProcessRepositroy;
import com.inquisition.inquisition.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccusationProcessServiceImpl {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    AccusationProcessRepositroy accusationProcessRepositroy;
    public Payload startProcess(AccusationProcessStartContainer container) {
        Integer processId = accusationProcessRepositroy.startAccusationProcess(
                container.getInquisitionProcessId()
        );
        return new PayloadWithInteger(200, "Inquisition process was created.", processId);
    }
    //FIXME: what return execute
    public Payload finishProcess(AccusationProcessWithIdContainer container) {
        int result = accusationProcessRepositroy.finishAccusationProcess(
                container.getAccusationId()
        );
        if (result == 0) {
            return new BasePayload(500, "Something goes wrong");
        }
        return new BasePayload(200, "Finished accusation process");
    }

    public Payload generateCases(AccusationProcessWithIdContainer container) {
        int result = accusationProcessRepositroy.generateCases(
                container.getAccusationId()
        );
        if (result == 0) {
            return new BasePayload(500, "Something goes wrong");
        }
        return new BasePayload(200, "Generated cases");
    }
}
