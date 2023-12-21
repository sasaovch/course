package com.inquisition.inquisition.model.cases;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InvestigativeCase {
    private Integer id;
    private LocalDate creationDate;
    private LocalDate closeDate;
}
