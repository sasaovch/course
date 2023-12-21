package com.inquisition.inquisition.mapper.investigativecase;

import com.inquisition.inquisition.model.cases.InvestigativeCase;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.INVESTIGATIVE_CASE_TABLE;

@Component
public class InvestigativeCaseRecordMapper {

    public InvestigativeCase mapInvestigativeCase(Record record) {
        InvestigativeCase investigativeCase = new InvestigativeCase();

        investigativeCase.setId(record.get(INVESTIGATIVE_CASE_TABLE.ID));
        investigativeCase.setCreationDate(record.get(INVESTIGATIVE_CASE_TABLE.CREATION_DATE));
        investigativeCase.setCloseDate(record.get(INVESTIGATIVE_CASE_TABLE.CLOSED_DATE));

        return investigativeCase;
    }
}
