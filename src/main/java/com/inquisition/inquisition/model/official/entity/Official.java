package com.inquisition.inquisition.model.official.entity;


import java.time.LocalDate;

import com.inquisition.inquisition.model.person.entity.Person;
import lombok.Data;

@Data
public class Official {
    private Integer id;
    private Integer personId;
    private OfficialName officialName;
    private LocalDate employmentDate;
    private LocalDate firedDate;
    private Person person;
}
