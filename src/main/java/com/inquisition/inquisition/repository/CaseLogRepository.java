package com.inquisition.inquisition.repository;

import com.inquisition.inquisition.models.enums.CaseLogResult;
import com.inquisition.inquisition.models.enums.CaseLogStatus;
import com.inquisition.inquisition.models.routines.MakeTortureStep;
import com.inquisition.inquisition.models.routines.StartDiscussion;
import com.inquisition.inquisition.models.routines.StartTorture;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.CASE_LOG_TABLE;

@Repository
public class CaseLogRepository {
    private final DSLContext dsl;

    public CaseLogRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional
    public void sendToDiscussion(Integer id) {
        StartDiscussion startDiscussion = new StartDiscussion();
        startDiscussion.setCurCaseId(id);
        startDiscussion.execute(dsl.configuration());
    }

    @Transactional
    public void finishProcess(Integer caseId, Integer result, String description, CaseLogStatus status) {
        dsl.update(CASE_LOG_TABLE)
                .set(CASE_LOG_TABLE.RESULT, result == 1 ? CaseLogResult.Admission : CaseLogResult.Denial)
                .set(CASE_LOG_TABLE.DESCRIPTION, description)
                .where(CASE_LOG_TABLE.CASE_ID.eq(caseId))
                .and(CASE_LOG_TABLE.CASE_STATUS.eq(status))
                .execute();
    }

    @Transactional
    public void sendToTorture(Integer id) {
        StartTorture startTorture = new StartTorture();
        startTorture.setCurCaseId(id);
        startTorture.execute(dsl.configuration());
    }

    @Transactional
    public void makeTortureStep(Integer id, Integer step) {
        MakeTortureStep makeTortureStep = new MakeTortureStep();
        makeTortureStep.setCurCaseId(id);
        makeTortureStep.setStep(step);
        makeTortureStep.execute(dsl.configuration());
    }
}
