package com.inquisition.inquisition.mapper.accusation;

import com.inquisition.inquisition.model.accusation.entity.AccusationProcess;
import com.inquisition.inquisition.model.accusation.entity.AccusationRecord;
import com.inquisition.inquisition.models.tables.GetNotResolvedAccusationRecord;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_PROCESS_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_RECORD_TABLE;

@Component
public class AccusationRecordMapper {

    public AccusationProcess mapAccusationProcess(Record record) {
        AccusationProcess process = new AccusationProcess();
        process.setId(record.get(ACCUSATION_PROCESS_TABLE.ID));
        process.setStartTime(record.get(ACCUSATION_PROCESS_TABLE.START_TIME));
        process.setFinishTime(record.get(ACCUSATION_PROCESS_TABLE.FINISH_TIME));
        process.setInquisitionProcessId(record.get(ACCUSATION_PROCESS_TABLE.INQUISITION_PROCESS_ID));
        return process;
    }

    public AccusationRecord mapAccusationRecord(Record record) {
        AccusationRecord accusationRecord = new AccusationRecord();

        accusationRecord.setId(record.get(ACCUSATION_RECORD_TABLE.ID));
        accusationRecord.setInformer(record.get(ACCUSATION_RECORD_TABLE.INFORMER));
        accusationRecord.setBishop(record.get(ACCUSATION_RECORD_TABLE.BISHOP));
        accusationRecord.setAccused(record.get(ACCUSATION_RECORD_TABLE.ACCUSED));
        accusationRecord.setViolationPlace(record.get(ACCUSATION_RECORD_TABLE.VIOLATION_PLACE));
        accusationRecord.setViolationTime(record.get(ACCUSATION_RECORD_TABLE.VIOLATION_TIME));
        accusationRecord.setDescription(record.get(ACCUSATION_RECORD_TABLE.DESCRIPTION));
        accusationRecord.setIdAccusation(record.get(ACCUSATION_RECORD_TABLE.ID_ACCUSATION));

        return accusationRecord;
    }

    public AccusationRecord mapNotResolvedAccusationRecordId(Record record) {
        AccusationRecord accusationRecord = new AccusationRecord();

        accusationRecord.setId(record.get(GetNotResolvedAccusationRecord.GET_NOT_RESOLVED_ACCUSATION_RECORD.GET_NOT_RESOLVED_ACCUSATION_RECORD_));

        return accusationRecord;
    }
}
