package com.inquisition.inquisition.model.church;

import java.time.LocalDate;

import com.inquisition.inquisition.model.locality.Locality;
import lombok.Data;


@Data
public class Church {
    private Integer id;
    private String name;
    private LocalDate foundationDate;
    private Locality locality;
}
