package com.inquisition.inquisition.repository;

import java.util.Optional;

import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialRepository extends JpaRepository<Official, Long> {
    Optional<Official> findByPersonId(Integer personId);
}
