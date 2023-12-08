package com.inquisition.inquisition.model.accusation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccusationRecordWithCasePayload {
    private Integer id;
    private String accused;
    private String violationPlace;
    private LocalDate creationDate;
    private String description;
}
