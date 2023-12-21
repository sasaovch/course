package com.inquisition.inquisition.model.official;


import java.time.LocalDate;

import com.inquisition.inquisition.model.person.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
