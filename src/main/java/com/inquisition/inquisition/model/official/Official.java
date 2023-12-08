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

//@Entity(name = "official")
@Data
public class Official {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name = "person_id", nullable = false)
    private Integer personId;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "official_name", nullable = false)
    private OfficialName officialName;

//    @Column(name = "employment_date", nullable = false)
    private LocalDate employmentDate;

//    @Column(name = "fired_date")
    private LocalDate firedDate;

    private Person person;
}
