package com.inquisition.inquisition.mapper.caselog;

import com.inquisition.inquisition.mapper.accusation.AccusationRecordMapper;
import com.inquisition.inquisition.mapper.investigativecase.InvestigativeCaseRecordMapper;
import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.cases.CaseLog;
import com.inquisition.inquisition.model.cases.InquisitionCaseLog;
import com.inquisition.inquisition.model.cases.InvestigativeCase;
import org.jooq.Record;
import org.springframework.stereotype.Component;

@Component
public class InquisitionCaseLogRecordMapper {
    private final AccusationRecordMapper accusationRecordMapper;
    private final InvestigativeCaseRecordMapper investigativeCaseRecordMapper;
    private final CaseLogRecordMapper caseLogRecordMapper;

    public InquisitionCaseLogRecordMapper(AccusationRecordMapper accusationRecordMapper,
                                          InvestigativeCaseRecordMapper investigativeCaseRecordMapper,
                                          CaseLogRecordMapper caseLogRecordMapper) {
        this.accusationRecordMapper = accusationRecordMapper;
        this.investigativeCaseRecordMapper = investigativeCaseRecordMapper;
        this.caseLogRecordMapper = caseLogRecordMapper;
    }

    public InquisitionCaseLog mapInquisitionCaseLog(Record record) {
        AccusationRecord accusationRecord = accusationRecordMapper.mapAccusationRecord(record);
        InvestigativeCase investigativeCase = investigativeCaseRecordMapper.mapInvestigativeCase(record);
        CaseLog caseLog = caseLogRecordMapper.mapCaseLog(record);

        InquisitionCaseLog inquisitionCaseLog = new InquisitionCaseLog();
        inquisitionCaseLog.setCaseLog(caseLog);
        inquisitionCaseLog.setInvestigativeCase(investigativeCase);
        inquisitionCaseLog.setAccusationRecord(accusationRecord);

        return inquisitionCaseLog;
    }
}
