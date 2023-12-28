package com.inquisition.inquisition.mapper.accusation;

import java.time.LocalDate;

import com.inquisition.inquisition.mapper.person.PersonRecordMapper;
import com.inquisition.inquisition.model.accusation.entity.AccusationRecordComplex;
import com.inquisition.inquisition.model.accusation.entity.AccusationRecordComplexWithCaseId;
import com.inquisition.inquisition.model.person.entity.Person;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_RECORD_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.ACCUSED_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.BISHOP_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INFORMER_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.INVESTIGATIVE_CASE_TABLE;

@Component
public class AccusationRecordComplexMapper {
    private final PersonRecordMapper personRecordMapper;

    public AccusationRecordComplexMapper(PersonRecordMapper personRecordMapper) {
        this.personRecordMapper = personRecordMapper;
    }

    public AccusationRecordComplex mapAccusationRecordComplex(Record record) {
        Integer id = record.get(ACCUSATION_RECORD_TABLE.ID);
        String violationPlace = record.get(ACCUSATION_RECORD_TABLE.VIOLATION_PLACE);
        LocalDate violationTime = record.get(ACCUSATION_RECORD_TABLE.VIOLATION_TIME);
        String description = record.get(ACCUSATION_RECORD_TABLE.DESCRIPTION);
        AccusationRecordComplex recordFull = new AccusationRecordComplex();

        Person informer = personRecordMapper.mapPerson(record, INFORMER_TABLE);
        Person bishop = personRecordMapper.mapPerson(record, BISHOP_TABLE);
        Person accused = personRecordMapper.mapPerson(record, ACCUSED_TABLE);

        recordFull.setId(id);
        recordFull.setViolationPlace(violationPlace);
        recordFull.setViolationTime(violationTime);
        recordFull.setDescription(description);
        recordFull.setInformer(informer);
        recordFull.setBishop(bishop);
        recordFull.setAccused(accused);

        return recordFull;
    }


    public AccusationRecordComplexWithCaseId mapAccusationRecordComplexWithCase(Record record) {
        AccusationRecordComplex recordFull = mapAccusationRecordComplex(record);

        AccusationRecordComplexWithCaseId result = new AccusationRecordComplexWithCaseId();

        Integer caseId = record.get(INVESTIGATIVE_CASE_TABLE.ID);
        LocalDate creationDate = record.get(INVESTIGATIVE_CASE_TABLE.CREATION_DATE);
        LocalDate closedDate = record.get(INVESTIGATIVE_CASE_TABLE.CLOSED_DATE);

        result.setInformer(recordFull.getInformer());
        result.setBishop(recordFull.getBishop());
        result.setAccused(recordFull.getAccused());
        result.setViolationPlace(recordFull.getViolationPlace());
        result.setViolationTime(recordFull.getViolationTime());
        result.setDescription(recordFull.getDescription());

        result.setCaseId(caseId);
        result.setCreationDate(creationDate);
        result.setClosedDate(closedDate);

        return result;
    }
}
