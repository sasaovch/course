package com.inquisition.inquisition.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.inquisition.inquisition.mapper.accusationrecord.AccusationRecordRecordMapper;
import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordFullWithCaseId;
import com.inquisition.inquisition.models.routines.AddAccusationRecord;
import com.inquisition.inquisition.models.tables.AccusationInvestigativeCase;
import com.inquisition.inquisition.models.tables.AccusationProcess;
import com.inquisition.inquisition.models.tables.GetNotResolvedAccusationRecord;
import com.inquisition.inquisition.models.tables.InvestigativeCase;
import com.inquisition.inquisition.models.tables.Person;
import com.inquisition.inquisition.models.tables.Violation;
import com.nimbusds.jose.util.Pair;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.jooq.impl.DSL.countDistinct;

@Repository
public class AccusationRecordRepository {
    private final DSLContext dsl;
    private final AccusationRecordRecordMapper accusationRecordRecordMapper;
    private static final com.inquisition.inquisition.models.tables.AccusationRecord ACCUSATION_RECORD =
            com.inquisition.inquisition.models.tables.AccusationRecord.ACCUSATION_RECORD;

    @Autowired
    public AccusationRecordRepository(DSLContext dsl, AccusationRecordRecordMapper accusationRecordRecordMapper) {
        this.dsl = dsl;
        this.accusationRecordRecordMapper = accusationRecordRecordMapper;
    }

    @Transactional(readOnly = true)
    public List<AccusationRecord> getNotResolvedAccusationRecord(Integer processId) {
        GetNotResolvedAccusationRecord result = new GetNotResolvedAccusationRecord().call(processId);

        return dsl.select(result.GET_NOT_RESOLVED_ACCUSATION_RECORD_)
                .from(result)
                .fetch()
                .map(r -> r.into(AccusationRecord.class));
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFull> getNotResolvedAccusationRecordWithSubFields(Integer processId) {
        GetNotResolvedAccusationRecord function = new GetNotResolvedAccusationRecord().call(processId);
        return findByCondition(DSL.condition(ACCUSATION_RECORD.ID.in(DSL.select(function.GET_NOT_RESOLVED_ACCUSATION_RECORD_).from(function))));
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFullWithCaseId> getNotResolvedAccusationRecordWithCases(Integer processId) {
        GetNotResolvedAccusationRecord function = new GetNotResolvedAccusationRecord().call(processId);
        return dsl.select()
                .from(ACCUSATION_RECORD)
                .join(Person.PERSON.as("bishop"))
                .on(Person.PERSON.as("bishop").ID.eq(ACCUSATION_RECORD.BISHOP))
                .join(Person.PERSON.as("informer"))
                .on(Person.PERSON.as("informer").ID.eq(ACCUSATION_RECORD.INFORMER))
                .join(Person.PERSON.as("accused"))
                .on(Person.PERSON.as("accused").ID.eq(ACCUSATION_RECORD.ACCUSED))
                .join(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE)
                .on(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE.RECORD_ID.eq(ACCUSATION_RECORD.ID))
                .join(InvestigativeCase.INVESTIGATIVE_CASE)
                .on(InvestigativeCase.INVESTIGATIVE_CASE.ID.eq(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE.CASE_ID))
                .where(DSL.condition(ACCUSATION_RECORD.ID.in(DSL.select(function.GET_NOT_RESOLVED_ACCUSATION_RECORD_).from(function))))
                .fetch()
                .map(accusationRecordRecordMapper::mapWithCase);
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFull> findByProcessId(Integer processId) {
        return findByCondition(DSL.condition(ACCUSATION_RECORD.ID_ACCUSATION.eq(processId)));
    }

    @Transactional(readOnly = true)
    public List<AccusationRecordFull> findByCondition(Condition condition) {
        return dsl.select()
                .from(ACCUSATION_RECORD)
                .join(Person.PERSON.as("bishop"))
                .on(Person.PERSON.as("bishop").ID.eq(ACCUSATION_RECORD.BISHOP))
                .join(Person.PERSON.as("informer"))
                .on(Person.PERSON.as("informer").ID.eq(ACCUSATION_RECORD.INFORMER))
                .join(Person.PERSON.as("accused"))
                .on(Person.PERSON.as("accused").ID.eq(ACCUSATION_RECORD.ACCUSED))
                .where(condition)
                .fetch()
                .map(accusationRecordRecordMapper::mapFull);
    }

    @Transactional(readOnly = true)
    public List<Pair<Integer, Integer>> findAllCases(List<Integer> accusationProcessIds) {
        return dsl.select(
                        AccusationProcess.ACCUSATION_PROCESS.ID,
                        countDistinct(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE.CASE_ID))
                .from(AccusationProcess.ACCUSATION_PROCESS)
                .join(ACCUSATION_RECORD)
                .on(ACCUSATION_RECORD.ID_ACCUSATION.eq(AccusationProcess.ACCUSATION_PROCESS.ID))
                .join(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE)
                .on(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE.RECORD_ID.eq(ACCUSATION_RECORD.ID))
                .where(AccusationProcess.ACCUSATION_PROCESS.ID.in(accusationProcessIds))
                .groupBy(AccusationProcess.ACCUSATION_PROCESS.ID)
                .fetch()
                .map(r -> Pair.of(r.value1(), r.value2()));
    }

    @Transactional
    public void addRecord(
            Integer accusationId,
            Integer accused,
            Integer bishop,
            LocalDateTime time,
            String description,
            Integer informer
    ) {
        AddAccusationRecord addAccusationRecord = new AddAccusationRecord();
        addAccusationRecord.setCurAccusationId(accusationId);
        addAccusationRecord.setCurAccused(accused);
        addAccusationRecord.setCurBishop(bishop);
        addAccusationRecord.setCurDateTime(time);
        addAccusationRecord.setCurDescription(description);
        addAccusationRecord.setCurInformer(informer);
        addAccusationRecord.execute(dsl.configuration());
    }

    public void connectCommandment(Integer commandment, Integer recordId) {
        dsl.insertInto(Violation.VIOLATION)
                .set(Violation.VIOLATION.COMMANDMENT_ID, commandment)
                .set(Violation.VIOLATION.RECORD_ID, recordId)
                .execute();
    }
}
