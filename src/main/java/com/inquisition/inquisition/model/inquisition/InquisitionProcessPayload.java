package com.inquisition.inquisition.model.inquisition;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InquisitionProcessPayload {
    private LocalDate startTime;
    private String locality;
    private String inquisitor;
    private Integer caseCount;
    private LocalDate endTime;
}
