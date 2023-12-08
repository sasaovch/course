package com.inquisition.inquisition.model.accusation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccusationRecordPayload {
    private Integer id;
    private String informer;
    private String bishop;
    private String accused;
    private String violationPlace;
    private LocalDateTime dateTime;
    private String description;
}
