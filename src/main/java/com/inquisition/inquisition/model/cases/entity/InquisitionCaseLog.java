package com.inquisition.inquisition.model.cases.entity;

import com.inquisition.inquisition.model.accusation.entity.AccusationRecord;
import com.inquisition.inquisition.model.investigativecase.entity.InvestigativeCase;
import lombok.Data;

@Data
public class InquisitionCaseLog {
    private AccusationRecord accusationRecord;
    private InvestigativeCase investigativeCase;
    private CaseLog caseLog;
}
