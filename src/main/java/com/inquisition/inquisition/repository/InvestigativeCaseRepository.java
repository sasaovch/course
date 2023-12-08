package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.models.tables.CaseLog;
import com.inquisition.inquisition.models.tables.InvestigativeCase;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InvestigativeCaseRepository {
    @Autowired
    DSLContext dsl;

    public List<com.inquisition.inquisition.model.cases.CaseLog> getCaseLogs(List<Integer> caseId) {
        return dsl.select()
                .from(CaseLog.CASE_LOG)
                .where(CaseLog.CASE_LOG.CASE_ID.in(caseId))
                .fetch()
                .map(r -> r.into(com.inquisition.inquisition.model.cases.CaseLog.class));
    }
}
