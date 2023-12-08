package com.inquisition.inquisition.model.accusation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.inquisition.inquisition.model.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccusationRecordFullWithCaseId extends AccusationRecordFull {
    private Integer caseId;
    private LocalDate creationDate;
    private LocalDate closedDate;
}
