package com.inquisition.inquisition.model.investigativecase.entity;

import com.inquisition.inquisition.model.commandment.entity.Commandment;
import lombok.Data;

@Data
public class CaseViolation {
    private Integer caseId;
    private Integer recordId;
    private Commandment commandment;
}
