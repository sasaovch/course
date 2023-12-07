package com.inquisition.inquisition.repository;

import java.util.List;
import java.util.Optional;

import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {
    Optional<Locality> findByName(String name);
}
