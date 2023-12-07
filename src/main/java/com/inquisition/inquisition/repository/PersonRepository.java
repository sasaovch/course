package com.inquisition.inquisition.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.inquisition.inquisition.model.person.Gender;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByNameAndSurnameAndBirthDateAndGenderAndLocalityId(
            String name, String surname, LocalDate birthDate, Gender gender, Integer localityId
    );
}
