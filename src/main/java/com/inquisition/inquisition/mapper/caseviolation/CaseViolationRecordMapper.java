package com.inquisition.inquisition.mapper.caseviolation;

import com.inquisition.inquisition.model.commandment.entity.Commandment;
import com.inquisition.inquisition.model.investigativecase.entity.CaseViolation;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_INVESTIGATIVE_CASE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.COMMANDMENT_TABLE;

@Component
public class CaseViolationRecordMapper {
    public CaseViolation mapCaseViolation(Record record) {
        CaseViolation caseViolation = new CaseViolation();

        Commandment commandment = new Commandment();
        commandment.setId(record.get(COMMANDMENT_TABLE.ID));
        commandment.setDescription(record.get(COMMANDMENT_TABLE.DESCRIPTION));
        commandment.setRank(record.get(COMMANDMENT_TABLE.RANK));

        caseViolation.setCommandment(commandment);
        caseViolation.setCaseId(record.get(ACCUSATION_INVESTIGATIVE_CASE_TABLE.CASE_ID));
        caseViolation.setRecordId(record.get(ACCUSATION_INVESTIGATIVE_CASE_TABLE.RECORD_ID));
        return caseViolation;
    }
}
