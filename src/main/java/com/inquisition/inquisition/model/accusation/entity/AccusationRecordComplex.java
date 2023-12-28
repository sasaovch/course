package com.inquisition.inquisition.model.accusation.entity;

import java.time.LocalDate;

import com.inquisition.inquisition.model.person.entity.Person;
import lombok.Data;

@Data
public class AccusationRecordComplex {
    private Integer id;
    private Person informer;
    private Person bishop;
    private Person accused;
    private String violationPlace;
    private LocalDate violationTime;
    private String description;
}
