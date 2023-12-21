package com.inquisition.inquisition.model.cases;

import com.inquisition.inquisition.model.accusation.AccusationRecord;
import lombok.Data;

@Data
public class InquisitionCaseLog {
    private AccusationRecord accusationRecord;
    private InvestigativeCase investigativeCase;
    private CaseLog caseLog;
}
