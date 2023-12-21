package com.inquisition.inquisition.model.person;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private Gender gender;
    private Integer localityId;
}
