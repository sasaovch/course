package com.inquisition.inquisition.model.person;

import java.time.LocalDate;

import com.inquisition.inquisition.model.locality.Locality;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@Entity(name = "person")
public class Person {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    @Column
    private String name;
//    @Column
    private String surname;
//    @Column
    private LocalDate birthDate;
//    @Enumerated(EnumType.STRING)
//    @Column(name = "person_gender")
    private Gender gender;
//    @OneToOne
//    @JoinColumn(name = "locality_id", nullable = false)
    private Integer localityId;
}
