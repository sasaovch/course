package com.inquisition.inquisition.repository;

import com.inquisition.inquisition.models.enums.CaseLogResult;
import com.inquisition.inquisition.models.enums.CaseLogStatus;
import com.inquisition.inquisition.models.routines.MakeTortureStep;
import com.inquisition.inquisition.models.routines.StartDiscussion;
import com.inquisition.inquisition.models.routines.StartTorture;
import com.inquisition.inquisition.models.tables.CaseLog;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CaseLogRepository {
    @Autowired
    DSLContext dsl;
    private static final CaseLog CASE_LOG = CaseLog.CASE_LOG;

    public void sendToDiscussion(Integer id) {
        StartDiscussion startDiscussion = new StartDiscussion();
        startDiscussion.setCurCaseId(id);
        startDiscussion.execute(dsl.configuration());
    }

    public void finishProcess(Integer id, Integer result) {
        dsl.update(CASE_LOG)
                .set(CASE_LOG.RESULT, result == 1 ? CaseLogResult.Admission : CaseLogResult.Denial)
                .where(CASE_LOG.ID.eq(id))
                .execute();
    }

    public void sendToTorture(Integer id) {
        StartTorture startTorture = new StartTorture();
        startTorture.setCurCaseId(id);
        startTorture.execute(dsl.configuration());
    }

    public void makeTortureStep(Integer id, Integer step) {
        MakeTortureStep makeTortureStep = new MakeTortureStep();
        makeTortureStep.setCurCaseId(id);
        makeTortureStep.setStep(step);
        makeTortureStep.execute(dsl.configuration());
    }
}
