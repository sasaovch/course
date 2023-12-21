package com.inquisition.inquisition.model.accusation;

import java.time.LocalDate;

import com.inquisition.inquisition.model.person.Person;
import lombok.Data;

@Data
public class AccusationRecordFull {
    private Integer id;
    private Person informer;
    private Person bishop;
    private Person accused;
    private String violationPlace;
    private LocalDate violationTime;
    private String description;
}
