package com.inquisition.inquisition.model.accusation.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AccusationRecordPayload {
    private Integer id;
    private String informer;
    private String bishop;
    private String accused;
    private String violationPlace;
    private LocalDate dateTime;
    private String description;
}
