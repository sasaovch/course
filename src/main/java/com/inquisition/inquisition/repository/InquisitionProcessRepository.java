package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.official.Official;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface InquisitionProcessRepository extends JpaRepository<InquisitionProcess, Long> {

    @Query("SELECT ip FROM inquisition_process ip JOIN ip.church c WHERE c.locality = :localityId AND ip.finishDate IS NULL")
    List<InquisitionProcess> findInProgressByLocalityId(@Param("localityId") Integer localityId);

    @Query("SELECT ip FROM inquisition_process ip JOIN ip.church c WHERE c.locality = :localityId")
    List<InquisitionProcess> findInquisitionProcessesByLocalityId(@Param("localityId") Integer localityId);

    // Define the procedure signature here
//    @Procedure(name = "start_inquisition_process")
//    Integer startInquisitionProcess(@Param("cur_official") Integer official, @Param("cur_church") Integer church, @Param("cur_bible") Integer bible);

    @Procedure(name = "my_procedure_name")
    void callMyProcedure(@Param("input_param") String inputParam);


}
