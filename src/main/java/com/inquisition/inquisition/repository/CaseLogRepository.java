package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.caselog.CaseLogRecordMapper;
import com.inquisition.inquisition.model.cases.InquisitionCase;
import com.inquisition.inquisition.models.enums.CaseLogResult;
import com.inquisition.inquisition.models.enums.CaseLogStatus;
import com.inquisition.inquisition.models.routines.MakeTortureStep;
import com.inquisition.inquisition.models.routines.StartDiscussion;
import com.inquisition.inquisition.models.routines.StartTorture;
import com.inquisition.inquisition.models.tables.AccusationInvestigativeCase;
import com.inquisition.inquisition.models.tables.AccusationProcess;
import com.inquisition.inquisition.models.tables.AccusationRecord;
import com.inquisition.inquisition.models.tables.CaseLog;
import com.inquisition.inquisition.models.tables.InvestigativeCase;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CaseLogRepository {
    @Autowired
    DSLContext dsl;
    private static final CaseLog CASE_LOG = CaseLog.CASE_LOG;

    @Transactional
    public void sendToDiscussion(Integer id) {
        StartDiscussion startDiscussion = new StartDiscussion();
        startDiscussion.setCurCaseId(id);
        startDiscussion.execute(dsl.configuration());
    }

    @Transactional
    public void finishProcess(Integer id, Integer result, String description) {
        dsl.update(CASE_LOG)
                .set(CASE_LOG.RESULT, result == 1 ? CaseLogResult.Admission : CaseLogResult.Denial)
                .set(CASE_LOG.DESCRIPTION, description)
                .where(CASE_LOG.ID.eq(id))
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

    public List<InquisitionCase> getCasesForDiscussion(Integer inquisitionId) {
        return dsl.select()
                .from(AccusationProcess.ACCUSATION_PROCESS)
                .join(AccusationRecord.ACCUSATION_RECORD)
                .on(AccusationRecord.ACCUSATION_RECORD.ID_ACCUSATION.eq(AccusationProcess.ACCUSATION_PROCESS.ID))
                .join(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE)
                .on(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE.RECORD_ID.eq(AccusationRecord.ACCUSATION_RECORD.ID))
                .join(InvestigativeCase.INVESTIGATIVE_CASE)
                .on(InvestigativeCase.INVESTIGATIVE_CASE.ID.eq(AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE.CASE_ID))
                .join(CaseLog.CASE_LOG)
                .on(CaseLog.CASE_LOG.CASE_ID.eq(InvestigativeCase.INVESTIGATIVE_CASE.ID))
                .where(AccusationProcess.ACCUSATION_PROCESS.INQUISITION_PROCESS_ID.eq(inquisitionId))
                .and(CaseLog.CASE_LOG.CASE_STATUS.eq(CaseLogStatus.Conversation))
                .and(CaseLog.CASE_LOG.RESULT.isNull())
                .fetch()
                .map(CaseLogRecordMapper::map);

    }
}
