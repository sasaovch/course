package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.caselog.InquisitionCaseLogRecordMapper;
import com.inquisition.inquisition.mapper.inquisition.InquisitionProcessRecordMapper;
import com.inquisition.inquisition.model.cases.InquisitionCaseLog;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.models.enums.CaseLogStatus;
import com.inquisition.inquisition.models.routines.FinishInquisitionProcess;
import com.inquisition.inquisition.models.routines.StartInquisitionProcess;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_INVESTIGATIVE_CASE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_PROCESS_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_RECORD_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.BIBLE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.CASE_LOG_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.CHURCH_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INQUISITION_PROCESS_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INVESTIGATIVE_CASE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.LOCALITY_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.OFFICIAL_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.PERSON_TABLE;

@Repository
public class InquisitionProcessRepository {
    private final DSLContext dsl;
    private final InquisitionProcessRecordMapper inquisitionProcessRecordMapper;
    private final InquisitionCaseLogRecordMapper inquisitionCaseLogRecordMapper;

    @Autowired
    public InquisitionProcessRepository(DSLContext dsl,
                                        InquisitionProcessRecordMapper inquisitionProcessRecordMapper,
                                        InquisitionCaseLogRecordMapper inquisitionCaseLogRecordMapper) {
        this.dsl = dsl;
        this.inquisitionProcessRecordMapper = inquisitionProcessRecordMapper;
        this.inquisitionCaseLogRecordMapper = inquisitionCaseLogRecordMapper;
    }

    @Transactional(readOnly = true)
    public List<InquisitionProcess> findAll() {
        return getComplexSelectQuery()
                .leftJoin(PERSON_TABLE)
                .on(PERSON_TABLE.ID.eq(OFFICIAL_TABLE.PERSON_ID))

                .fetch()
                .map(inquisitionProcessRecordMapper::mapInquisitionProcessWithPerson);
    }

    @Transactional(readOnly = true)
    public List<InquisitionProcess> findInProgressByOfficialId(Integer officialId) {
        return getComplexSelectQuery()
                .where(INQUISITION_PROCESS_TABLE.FINISH_DATA.isNull())
                .and(INQUISITION_PROCESS_TABLE.OFFICIAL_ID.eq(officialId))

                .fetch()
                .map(inquisitionProcessRecordMapper::mapInquisitionProcess);
    }

    @Transactional(readOnly = true)
    public List<InquisitionProcess> findInProgressByChurchId(Integer churchId) {
        return getComplexSelectQuery()
                .where(INQUISITION_PROCESS_TABLE.FINISH_DATA.isNull())
                .and(INQUISITION_PROCESS_TABLE.CHURCH_ID.eq(churchId))

                .fetch()
                .map(inquisitionProcessRecordMapper::mapInquisitionProcess);
    }

    @Transactional(readOnly = true)
    public List<InquisitionCaseLog> getCasesForDiscussion(Integer inquisitionId) {
        return getCaseForCondition(DSL.condition(ACCUSATION_PROCESS_TABLE.INQUISITION_PROCESS_ID.eq(inquisitionId))
                .and(CASE_LOG_TABLE.CASE_STATUS.eq(CaseLogStatus.Conversation))
                .and(CASE_LOG_TABLE.RESULT.isNull()));
    }

    @Transactional(readOnly = true)
    public List<InquisitionCaseLog> getCasesForTorture(Integer inquisitionId) {
        return getCaseForCondition(DSL.condition(ACCUSATION_PROCESS_TABLE.INQUISITION_PROCESS_ID.eq(inquisitionId))
                .and(CASE_LOG_TABLE.CASE_STATUS.eq(CaseLogStatus.Torture))
                .and(CASE_LOG_TABLE.RESULT.isNull()));
    }

    @Transactional(readOnly = true)
    public List<InquisitionCaseLog> getCasesForPunishment(Integer inquisitionId) {
        return getCaseForCondition(DSL.condition(ACCUSATION_PROCESS_TABLE.INQUISITION_PROCESS_ID.eq(inquisitionId))
                .and(CASE_LOG_TABLE.CASE_STATUS.eq(CaseLogStatus.Punishment))
                .and(CASE_LOG_TABLE.RESULT.isNull()));
    }

    private List<InquisitionCaseLog> getCaseForCondition(Condition condition) {
        return dsl.select()
                .from(ACCUSATION_PROCESS_TABLE)

                .join(ACCUSATION_RECORD_TABLE)
                .on(ACCUSATION_RECORD_TABLE.ID_ACCUSATION.eq(ACCUSATION_PROCESS_TABLE.ID))

                .join(ACCUSATION_INVESTIGATIVE_CASE_TABLE)
                .on(ACCUSATION_INVESTIGATIVE_CASE_TABLE.RECORD_ID.eq(ACCUSATION_RECORD_TABLE.ID))

                .join(INVESTIGATIVE_CASE_TABLE)
                .on(INVESTIGATIVE_CASE_TABLE.ID.eq(ACCUSATION_INVESTIGATIVE_CASE_TABLE.CASE_ID))

                .join(CASE_LOG_TABLE)
                .on(CASE_LOG_TABLE.CASE_ID.eq(INVESTIGATIVE_CASE_TABLE.ID))

                .where(condition)
                .fetch()
                .map(inquisitionCaseLogRecordMapper::mapInquisitionCaseLog);
    }

    @Transactional
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

    private SelectOnConditionStep<Record> getComplexSelectQuery() {
        return dsl.select()
                .from(INQUISITION_PROCESS_TABLE)

                .leftJoin(CHURCH_TABLE)
                .on(CHURCH_TABLE.ID.eq(INQUISITION_PROCESS_TABLE.CHURCH_ID))

                .leftJoin(ACCUSATION_PROCESS_TABLE)
                .on(ACCUSATION_PROCESS_TABLE.INQUISITION_PROCESS_ID.eq(INQUISITION_PROCESS_TABLE.ID))

                .leftJoin(BIBLE_TABLE)
                .on(BIBLE_TABLE.VERSION.eq(INQUISITION_PROCESS_TABLE.BIBLE_ID))

                .leftJoin(OFFICIAL_TABLE)
                .on(OFFICIAL_TABLE.ID.eq(INQUISITION_PROCESS_TABLE.OFFICIAL_ID))

                .join(LOCALITY_TABLE)
                .on(LOCALITY_TABLE.ID.eq(CHURCH_TABLE.LOCALITY_ID));
    }
}
