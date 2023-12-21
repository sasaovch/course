package com.inquisition.inquisition.mapper.caselog;

import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.cases.CaseLog;
import com.inquisition.inquisition.model.cases.InquisitionCase;
import com.inquisition.inquisition.model.cases.InvestigativeCase;
import com.inquisition.inquisition.models.tables.records.AccusationRecordRecord;
import org.jooq.Record;
import org.jooq.RecordMapper;

public class CaseLogRecordMapper {
    public static InquisitionCase map(Record record) {
        AccusationRecord accusationRecord = record.into(AccusationRecord.class);
        InvestigativeCase investigativeCase = record.into(InvestigativeCase.class);
        CaseLog caseLog = record.into(CaseLog.class);

        InquisitionCase inquisitionCase = new InquisitionCase();
        inquisitionCase.setCaseLog(caseLog);
        inquisitionCase.setInvestigativeCase(investigativeCase);
        inquisitionCase.setAccusationRecord(accusationRecord);

        return inquisitionCase;
    }
}
