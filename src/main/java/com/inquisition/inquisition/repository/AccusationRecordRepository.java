package com.inquisition.inquisition.repository;

import java.time.LocalDate;
import java.util.List;

import com.inquisition.inquisition.mapper.accusation.AccusationRecordComplexMapper;
import com.inquisition.inquisition.mapper.accusation.AccusationRecordMapper;
import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordFullWithCaseId;
import com.inquisition.inquisition.models.routines.AddAccusationRecord;
import com.inquisition.inquisition.models.routines.ConnectCommandmentWithRecord;
import com.inquisition.inquisition.models.tables.GetNotResolvedAccusationRecord;
import com.nimbusds.jose.util.Pair;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_INVESTIGATIVE_CASE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_PROCESS_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_RECORD_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSED_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.BISHOP_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INFORMER_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INVESTIGATIVE_CASE_TABLE;
import static org.jooq.impl.DSL.countDistinct;

@Repository
public class AccusationRecordRepository {
    private final DSLContext dsl;
    private final AccusationRecordMapper accusationRecordMapper;
    private final AccusationRecordComplexMapper accusationRecordComplexMapper;

    @Autowired
    public AccusationRecordRepository(DSLContext dsl,
                                      AccusationRecordMapper accusationRecordMapper,
                                      AccusationRecordComplexMapper accusationRecordComplexMapper) {
        this.dsl = dsl;
        this.accusationRecordMapper = accusationRecordMapper;
        this.accusationRecordComplexMapper = accusationRecordComplexMapper;
    }

    @Transactional(readOnly = true)
    public List<AccusationRecord> getNotResolvedAccusationRecord(Integer processId) {
        GetNotResolvedAccusationRecord result = new GetNotResolvedAccusationRecord().call(processId);

        return dsl.select(result.GET_NOT_RESOLVED_ACCUSATION_RECORD_)
                .from(result)
                .fetch()
                .map(accusationRecordMapper::mapNotResolvedAccusationRecordId);
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFull> getNotResolvedAccusationRecordWithSubFields(Integer processId) {
        GetNotResolvedAccusationRecord function = new GetNotResolvedAccusationRecord().call(processId);
        return findByCondition(DSL.condition(ACCUSATION_RECORD_TABLE.ID.in(DSL.select(function.GET_NOT_RESOLVED_ACCUSATION_RECORD_).from(function))));
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFullWithCaseId> getNotResolvedAccusationRecordWithCases(Integer processId) {
        GetNotResolvedAccusationRecord function = new GetNotResolvedAccusationRecord().call(processId);

        return dsl.select()
                .from(ACCUSATION_RECORD_TABLE)
                .join(BISHOP_TABLE)
                .on(BISHOP_TABLE.ID.eq(ACCUSATION_RECORD_TABLE.BISHOP))
                .join(INFORMER_TABLE)
                .on(INFORMER_TABLE.ID.eq(ACCUSATION_RECORD_TABLE.INFORMER))
                .join(ACCUSED_TABLE)
                .on(ACCUSED_TABLE.ID.eq(ACCUSATION_RECORD_TABLE.ACCUSED))
                .join(ACCUSATION_INVESTIGATIVE_CASE_TABLE)
                .on(ACCUSATION_INVESTIGATIVE_CASE_TABLE.RECORD_ID.eq(ACCUSATION_RECORD_TABLE.ID))
                .join(INVESTIGATIVE_CASE_TABLE)
                .on(INVESTIGATIVE_CASE_TABLE.ID.eq(ACCUSATION_INVESTIGATIVE_CASE_TABLE.CASE_ID))
                .where(DSL.condition(ACCUSATION_RECORD_TABLE.ID.in(DSL.select(function.GET_NOT_RESOLVED_ACCUSATION_RECORD_).from(function))))
                .fetch()
                .map(accusationRecordComplexMapper::mapAccusationRecordComplexWithCase);
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFull> findByProcessId(Integer processId) {
        return findByCondition(DSL.condition(ACCUSATION_RECORD_TABLE.ID_ACCUSATION.eq(processId)));
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFull> findByCondition(Condition condition) {
        return dsl.select()
                .from(ACCUSATION_RECORD_TABLE)
                .join(BISHOP_TABLE)
                .on(BISHOP_TABLE.ID.eq(ACCUSATION_RECORD_TABLE.BISHOP))
                .join(INFORMER_TABLE)
                .on(INFORMER_TABLE.ID.eq(ACCUSATION_RECORD_TABLE.INFORMER))
                .join(ACCUSED_TABLE)
                .on(ACCUSED_TABLE.ID.eq(ACCUSATION_RECORD_TABLE.ACCUSED))
                .where(condition)
                .fetch()
                .map(accusationRecordComplexMapper::mapAccusationRecordComplex);
    }

    @Transactional(readOnly = true)
    public List<Pair<Integer, Integer>> findAllCases(List<Integer> accusationProcessIds) {
        return dsl.select(
                        ACCUSATION_PROCESS_TABLE.ID,
                        countDistinct(ACCUSATION_INVESTIGATIVE_CASE_TABLE.CASE_ID))
                .from(ACCUSATION_PROCESS_TABLE)
                .join(ACCUSATION_RECORD_TABLE)
                .on(ACCUSATION_RECORD_TABLE.ID_ACCUSATION.eq(ACCUSATION_PROCESS_TABLE.ID))
                .join(ACCUSATION_INVESTIGATIVE_CASE_TABLE)
                .on(ACCUSATION_INVESTIGATIVE_CASE_TABLE.RECORD_ID.eq(ACCUSATION_RECORD_TABLE.ID))
                .where(ACCUSATION_PROCESS_TABLE.ID.in(accusationProcessIds))
                .groupBy(ACCUSATION_PROCESS_TABLE.ID)
                .fetch()
                .map(r -> Pair.of(r.value1(), r.value2()));
    }

    @Transactional
    public void addRecord(
            Integer accusationId,
            Integer accused,
            Integer bishop,
            LocalDate date,
            String description,
            Integer informer,
            String violationPlace
    ) {
        AddAccusationRecord addAccusationRecord = new AddAccusationRecord();
        addAccusationRecord.setCurAccusationId(accusationId);
        addAccusationRecord.setCurAccused(accused);
        addAccusationRecord.setCurBishop(bishop);
        addAccusationRecord.setCurDate(date);
        addAccusationRecord.setCurDescription(description);
        addAccusationRecord.setCurInformer(informer);
        addAccusationRecord.setCurViolationPlace(violationPlace);
        addAccusationRecord.execute(dsl.configuration());
    }

    @Transactional
    public void connectCommandment(Integer commandment, Integer recordId) {
        ConnectCommandmentWithRecord helper = new ConnectCommandmentWithRecord();
        helper.setCurCommandmentId(commandment);
        helper.setCurRecordId(recordId);
        helper.execute(dsl.configuration());
    }
}
