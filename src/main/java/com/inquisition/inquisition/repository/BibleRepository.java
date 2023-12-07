package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.official.Official;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BibleRepository extends JpaRepository<Bible, Long> {
}
