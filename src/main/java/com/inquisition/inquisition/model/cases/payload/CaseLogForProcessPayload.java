package com.inquisition.inquisition.model.cases.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CaseLogForProcessPayload {
    private String informer;
    private String bishop;
    private String accused;
    private String violationPlace;
    private LocalDate dateTime;
    private String description;
}
