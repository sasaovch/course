package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface AccusationRecordRepository extends JpaRepository<AccusationRecord, Long> {
    List<AccusationRecord> findAccusationRecordsByAccusationProcessId(Long accusationProcessId);

    @Procedure(name = "get_not_resolved_accusation_record")
    List<AccusationRecord> getNotResolvedAccusationRecord(@Param("cur_accusation_id") Integer accusationId);
}
