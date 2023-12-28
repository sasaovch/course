package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.accusation.AccusationRecordComplexMapper;
import com.inquisition.inquisition.mapper.caseviolation.CaseViolationRecordMapper;
import com.inquisition.inquisition.model.accusation.entity.AccusationInvestigativeCase;
import com.inquisition.inquisition.model.accusation.entity.AccusationRecordComplexWithCaseId;
import com.inquisition.inquisition.model.investigativecase.entity.CaseViolation;
import com.inquisition.inquisition.models.tables.GetNotResolvedCases;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_INVESTIGATIVE_CASE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_RECORD_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSED_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.BISHOP_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.COMMANDMENT_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INFORMER_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INVESTIGATIVE_CASE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.VIOLATION_TABLE;

@Repository
public class InvestigativeCaseRepository {
    private final DSLContext dsl;
    private final AccusationRecordComplexMapper accusationRecordComplexMapper;
    private final CaseViolationRecordMapper caseViolationRecordMapper;

    @Autowired
    public InvestigativeCaseRepository(DSLContext dsl,
                                       AccusationRecordComplexMapper accusationRecordComplexMapper,
                                       CaseViolationRecordMapper caseViolationRecordMapper) {
        this.dsl = dsl;
        this.accusationRecordComplexMapper = accusationRecordComplexMapper;
        this.caseViolationRecordMapper = caseViolationRecordMapper;
    }


    @Transactional(readOnly = true)
    public List<AccusationRecordComplexWithCaseId> getNotResolvedCasesWithAccusationRecord(Integer inquisitionProcessId) {
        GetNotResolvedCases function = new GetNotResolvedCases().call(inquisitionProcessId);

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

                .where(DSL.condition(INVESTIGATIVE_CASE_TABLE.ID.in(DSL.select(function.GET_NOT_RESOLVED_CASES_).from(function))))
                .fetch()
                .map(accusationRecordComplexMapper::mapAccusationRecordComplexWithCase);
    }

    @Transactional(readOnly = true)
    public List<AccusationInvestigativeCase> getNotResolvedCasesIdWithAccusationRecordId(Integer inquisitionProcessId) {
        GetNotResolvedCases function = new GetNotResolvedCases().call(inquisitionProcessId);

        return dsl.select()
                .from(ACCUSATION_INVESTIGATIVE_CASE_TABLE)
                .where(
                        DSL.condition(ACCUSATION_INVESTIGATIVE_CASE_TABLE.CASE_ID.in(
                                DSL.select(function.GET_NOT_RESOLVED_CASES_).from(function))
                        )
                )

                .fetch()
                .map(r -> r.into(AccusationInvestigativeCase.class));
    }

    @Transactional(readOnly = true)
    public List<CaseViolation> getCaseViolations(Integer caseId) {
        return dsl.select()
                .from(ACCUSATION_INVESTIGATIVE_CASE_TABLE)
                .join(VIOLATION_TABLE)
                .on(VIOLATION_TABLE.RECORD_ID.eq(ACCUSATION_INVESTIGATIVE_CASE_TABLE.RECORD_ID))
                .join(COMMANDMENT_TABLE)
                .on(COMMANDMENT_TABLE.ID.eq(VIOLATION_TABLE.COMMANDMENT_ID))
                .where(ACCUSATION_INVESTIGATIVE_CASE_TABLE.CASE_ID.eq(caseId))
                .fetch()
                .map(caseViolationRecordMapper::mapCaseViolation);
    }
}
