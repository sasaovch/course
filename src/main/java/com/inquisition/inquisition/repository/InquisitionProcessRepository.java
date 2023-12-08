package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.inquisitionprocess.InquisitionProcessRecordMapper;
import com.inquisition.inquisition.mapper.inquisitionprocess.InquisitionProcessRecordUnmapper;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.models.routines.FinishInquisitionProcess;
import com.inquisition.inquisition.models.routines.StartInquisitionProcess;
import com.inquisition.inquisition.models.tables.AccusationProcess;
import com.inquisition.inquisition.models.tables.Bible;
import com.inquisition.inquisition.models.tables.Church;
import com.inquisition.inquisition.models.tables.Locality;
import com.inquisition.inquisition.models.tables.Official;
import com.inquisition.inquisition.models.tables.Person;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InquisitionProcessRepository implements CrudRepository<InquisitionProcess> {
    private final DSLContext dsl;
    private final InquisitionProcessRecordMapper inquisitionProcessRecordMapper;
    private final InquisitionProcessRecordUnmapper inquisitionProcessRecordUnmapper;
    private static final com.inquisition.inquisition.models.tables.InquisitionProcess INQUISITION_PROCESS =
            com.inquisition.inquisition.models.tables.InquisitionProcess.INQUISITION_PROCESS;

    @Autowired
    public InquisitionProcessRepository(DSLContext dsl,
                                        InquisitionProcessRecordMapper inquisitionProcessRecordMapper,
                                        InquisitionProcessRecordUnmapper inquisitionProcessRecordUnmapper) {
        this.dsl = dsl;
        this.inquisitionProcessRecordMapper = inquisitionProcessRecordMapper;
        this.inquisitionProcessRecordUnmapper = inquisitionProcessRecordUnmapper;
    }

    @Override
    public InquisitionProcess insert(InquisitionProcess inquisitionProcess) {
        return null;
    }

    @Override
    public InquisitionProcess update(InquisitionProcess inquisitionProcess) {
        return null;
    }

    @Override
    public InquisitionProcess find(Integer id) {
        return null;
    }

    @Override
    public List<InquisitionProcess> findAll(Condition condition) {
        return dsl.selectFrom(INQUISITION_PROCESS)
                .where(condition)
                .fetch()
                .map(r -> r.into(InquisitionProcess.class));
    }

    public List<InquisitionProcess> findByLocalityId(Integer localityId) {
        return dsl.select()
                .from(INQUISITION_PROCESS)
                .leftJoin(Church.CHURCH)
                .on(Church.CHURCH.ID.eq(INQUISITION_PROCESS.CHURCH_ID))
                .leftJoin(AccusationProcess.ACCUSATION_PROCESS)
                .on(AccusationProcess.ACCUSATION_PROCESS.INQUISITION_PROCESS_ID.eq(INQUISITION_PROCESS.ID))
                .leftJoin(Bible.BIBLE)
                .on(Bible.BIBLE.VERSION.eq(INQUISITION_PROCESS.BIBLE_ID))
                .leftJoin(Official.OFFICIAL)
                .on(Official.OFFICIAL.ID.eq(INQUISITION_PROCESS.OFFICIAL_ID))
                .join(Locality.LOCALITY)
                .on(Locality.LOCALITY.ID.eq(Church.CHURCH.LOCALITY_ID))
                .where(Church.CHURCH.LOCALITY_ID.eq(localityId))
                .fetch()
                .map(r -> inquisitionProcessRecordMapper.mapFull(r));
    }

    public List<InquisitionProcess> findAll() {
        return dsl.select()
                .from(INQUISITION_PROCESS)
                .leftJoin(Church.CHURCH)
                .on(Church.CHURCH.ID.eq(INQUISITION_PROCESS.CHURCH_ID))
                .leftJoin(AccusationProcess.ACCUSATION_PROCESS)
                .on(AccusationProcess.ACCUSATION_PROCESS.INQUISITION_PROCESS_ID.eq(INQUISITION_PROCESS.ID))
                .leftJoin(Bible.BIBLE)
                .on(Bible.BIBLE.VERSION.eq(INQUISITION_PROCESS.BIBLE_ID))
                .leftJoin(Official.OFFICIAL)
                .on(Official.OFFICIAL.ID.eq(INQUISITION_PROCESS.OFFICIAL_ID))
                .leftJoin(Person.PERSON)
                .on(Person.PERSON.ID.eq(Official.OFFICIAL.PERSON_ID))
                .join(Locality.LOCALITY)
                .on(Locality.LOCALITY.ID.eq(Church.CHURCH.LOCALITY_ID))
                .fetch()
                .map(inquisitionProcessRecordMapper::mapFull);
    }

    @Override
    public Boolean delete(Integer id) {
        return null;
    }

    public List<InquisitionProcess> findInProgressByLocalityId(Integer localityId) {
        return dsl.select()
                .from(INQUISITION_PROCESS)
                .leftJoin(Church.CHURCH)
                .on(Church.CHURCH.ID.eq(INQUISITION_PROCESS.CHURCH_ID))
                .leftJoin(AccusationProcess.ACCUSATION_PROCESS)
                .on(AccusationProcess.ACCUSATION_PROCESS.INQUISITION_PROCESS_ID.eq(INQUISITION_PROCESS.ID))
                .leftJoin(Bible.BIBLE)
                .on(Bible.BIBLE.VERSION.eq(INQUISITION_PROCESS.BIBLE_ID))
                .leftJoin(Official.OFFICIAL)
                .on(Official.OFFICIAL.ID.eq(INQUISITION_PROCESS.OFFICIAL_ID))
                .join(Locality.LOCALITY)
                .on(Locality.LOCALITY.ID.eq(Church.CHURCH.LOCALITY_ID))
                .where(INQUISITION_PROCESS.FINISH_DATA.isNull())
                .and(Church.CHURCH.LOCALITY_ID.eq(localityId))
                .fetch()
                .map(r -> inquisitionProcessRecordMapper.mapFull(r));
    }

    public List<InquisitionProcess> findInProgressByOfficialId(Integer officialId) {
        return dsl.select()
                .from(INQUISITION_PROCESS)
                .leftJoin(Church.CHURCH)
                .on(Church.CHURCH.ID.eq(INQUISITION_PROCESS.CHURCH_ID))
                .leftJoin(AccusationProcess.ACCUSATION_PROCESS)
                .on(AccusationProcess.ACCUSATION_PROCESS.INQUISITION_PROCESS_ID.eq(INQUISITION_PROCESS.ID))
                .leftJoin(Bible.BIBLE)
                .on(Bible.BIBLE.VERSION.eq(INQUISITION_PROCESS.BIBLE_ID))
                .leftJoin(Official.OFFICIAL)
                .on(Official.OFFICIAL.ID.eq(INQUISITION_PROCESS.OFFICIAL_ID))
                .join(Locality.LOCALITY)
                .on(Locality.LOCALITY.ID.eq(Church.CHURCH.LOCALITY_ID))
                .where(INQUISITION_PROCESS.FINISH_DATA.isNull())
                .and(Church.CHURCH.LOCALITY_ID.eq(officialId))
                .fetch()
                .map(inquisitionProcessRecordMapper::mapFull);
    }

    public Integer startInquisitionProcess(
            Integer officialId,
            Integer churchId,
            Integer bibleId
    ) {
        StartInquisitionProcess startInquisitionProcess = new StartInquisitionProcess();
        startInquisitionProcess.setCurBible(bibleId);
        startInquisitionProcess.setCurChurch(churchId);
        startInquisitionProcess.setCurOfficial(officialId);

        startInquisitionProcess.execute(dsl.configuration());
        return startInquisitionProcess.getReturnValue();
    }

    public Integer finishInquisitionProcess(
            Integer processId
    ) {
        FinishInquisitionProcess finishInquisitionProcess = new FinishInquisitionProcess();
        finishInquisitionProcess.setCurInquisitionProcessId(processId);

        finishInquisitionProcess.execute(dsl.configuration());
        return finishInquisitionProcess.getReturnValue();
    }
}
//
//    @Query("SELECT ip FROM inquisition_process ip JOIN ip.church c WHERE c.locality = :localityId AND ip.finishDate
//    IS NULL")
//    List<InquisitionProcess> findInProgressByLocalityId(@Param("localityId") Integer localityId);
//
//    @Query("SELECT ip FROM inquisition_process ip JOIN ip.church c WHERE c.locality = :localityId")
//    List<InquisitionProcess> findInquisitionProcessesByLocalityId(@Param("localityId") Integer localityId);
//
//    // Define the procedure signature here
////    @Procedure(name = "start_inquisition_process")
////    Integer startInquisitionProcess(@Param("cur_official") Integer official, @Param("cur_church") Integer church,
// @Param("cur_bible") Integer bible);
//
//    @Procedure(name = "my_procedure_name")
//    void callMyProcedure(@Param("input_param") String inputParam);
//
//
//}
