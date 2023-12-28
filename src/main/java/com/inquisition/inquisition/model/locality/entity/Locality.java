package com.inquisition.inquisition.model.locality.entity;


import java.time.LocalDate;

import lombok.Data;

@Data
public class Locality {
    private Integer id;
    private String name;
    private LocalDate foundationDate;
}
