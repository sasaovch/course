package com.inquisition.inquisition.model.church.entity;

import java.time.LocalDate;

import com.inquisition.inquisition.model.locality.entity.Locality;
import lombok.Data;


@Data
public class Church {
    private Integer id;
    private String name;
    private LocalDate foundationDate;
    private Locality locality;
}
