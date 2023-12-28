package com.inquisition.inquisition.model.cases.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CaseLogPayload {
    private Integer id;
    private String accused;
    private LocalDate creationDate;
    private String description;
    private String violationDescription;
    private String status;
    private Integer step;
}
