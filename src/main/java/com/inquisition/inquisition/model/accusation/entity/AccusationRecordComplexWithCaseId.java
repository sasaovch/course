package com.inquisition.inquisition.model.accusation.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccusationRecordComplexWithCaseId extends AccusationRecordComplex {
    private Integer caseId;
    private LocalDate creationDate;
    private LocalDate closedDate;
}
