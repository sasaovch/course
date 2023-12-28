package com.inquisition.inquisition.model.cases.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CaseLogForPunishmentPayload {
    private String accused;
    private String punishment;
    private String prisonName;
    private String violationDescription;
    private LocalDate creationDate;
    private String description;
}
