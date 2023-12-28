package com.inquisition.inquisition.model.accusation.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AccusationRecord {
    private Integer id;
    private Integer informer;
    private Integer bishop;
    private Integer accused;
    private String violationPlace;
    private LocalDate violationTime;
    private String description;
    private Integer idAccusation;
}
