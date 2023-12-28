package com.inquisition.inquisition.mapper.caselog;

import com.inquisition.inquisition.model.cases.entity.CaseLog;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.CASE_LOG_TABLE;

@Component
public class CaseLogRecordMapper {
    public CaseLog mapCaseLog(Record record) {
        CaseLog caseLog = new CaseLog();

        caseLog.setId(record.get(CASE_LOG_TABLE.ID));
        caseLog.setCaseId(record.get(CASE_LOG_TABLE.CASE_ID));
        caseLog.setStatus(record.get(CASE_LOG_TABLE.CASE_STATUS));
        caseLog.setPrincipal(record.get(CASE_LOG_TABLE.PRINCIPAL));
        caseLog.setStartTime(record.get(CASE_LOG_TABLE.START_TIME));
        caseLog.setResult(record.get(CASE_LOG_TABLE.RESULT));
        caseLog.setPrisonId(record.get(CASE_LOG_TABLE.PRISON_ID));
        caseLog.setFinishTime(record.get(CASE_LOG_TABLE.FINISH_TIME));
        caseLog.setPunishmentId(record.get(CASE_LOG_TABLE.PUNISHMENT_ID));
        caseLog.setDescription(record.get(CASE_LOG_TABLE.DESCRIPTION));

        return caseLog;

    }
}
