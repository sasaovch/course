package com.inquisition.inquisition.mapper.accusationrecord;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordFullWithCaseId;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.models.tables.InvestigativeCase;
import com.inquisition.inquisition.models.tables.records.AccusationRecordRecord;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.jooq.RecordMapper;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.mapper.person.PersonRecordMapper.convertGender;

@RequiredArgsConstructor
@Component
public class AccusationRecordRecordMapper implements RecordMapper<AccusationRecordRecord, AccusationRecord> {
    @Override
    public @Nullable AccusationRecord map(AccusationRecordRecord accusationRecordRecord) {
        return null;
    }
//FIXME: проверить обязательно
    public AccusationRecordFull mapFull(Record record) {
        Integer id = record.get(com.inquisition.inquisition.models.tables.AccusationRecord.ACCUSATION_RECORD.ID);
        String violationPlace = record.get(com.inquisition.inquisition.models.tables.AccusationRecord.ACCUSATION_RECORD.VIOLATION_PLACE);
        LocalDateTime violationTime = record.get(com.inquisition.inquisition.models.tables.AccusationRecord.ACCUSATION_RECORD.VIOLATION_TIME);
        String description = record.get(com.inquisition.inquisition.models.tables.AccusationRecord.ACCUSATION_RECORD.DESCRIPTION);
        AccusationRecordFull recordFull = new AccusationRecordFull();
//                record.into(AccusationRecordFull.class);

        Person informer = convertToPerson("informer", record);
        Person bishop = convertToPerson("bishop", record);
        Person accused = convertToPerson("accused", record);

        recordFull.setId(id);
        recordFull.setViolationPlace(violationPlace);
        recordFull.setViolationTime(violationTime);
        recordFull.setDescription(description);
        recordFull.setInformer(informer);
        recordFull.setBishop(bishop);
        recordFull.setAccused(accused);
        return recordFull;
    }

    public static Person convertToPerson(String alias, Record record) {
        return new Person(
                record.get(com.inquisition.inquisition.models.tables.Person.PERSON.as(alias).ID),
                record.get(com.inquisition.inquisition.models.tables.Person.PERSON.as(alias).NAME),
                record.get(com.inquisition.inquisition.models.tables.Person.PERSON.as(alias).SURNAME),
                record.get(com.inquisition.inquisition.models.tables.Person.PERSON.as(alias).BIRTH_DATE),
                convertGender(record.get(com.inquisition.inquisition.models.tables.Person.PERSON.as(alias).PERSON_GENDER)),
                record.get(com.inquisition.inquisition.models.tables.Person.PERSON.as(alias).LOCALITY_ID)
        );
    }


    public AccusationRecordFullWithCaseId mapWithCase(Record record) {
        AccusationRecordFull recordFull = mapFull(record);

        AccusationRecordFullWithCaseId result = new AccusationRecordFullWithCaseId();

        Integer caseId = record.get(InvestigativeCase.INVESTIGATIVE_CASE.ID);
        LocalDate creationDate = record.get(InvestigativeCase.INVESTIGATIVE_CASE.CREATION_DATE);
        LocalDate closedDate = record.get(InvestigativeCase.INVESTIGATIVE_CASE.CLOSED_DATE);
//        String description = record.get(com.inquisition.inquisition.models.tables.AccusationRecord.ACCUSATION_RECORD.DESCRIPTION);

        result.setInformer(recordFull.getInformer());
        result.setBishop(recordFull.getBishop());
        result.setAccused(recordFull.getAccused());
        result.setViolationPlace(recordFull.getViolationPlace());
        result.setDescription(recordFull.getDescription());

        result.setCaseId(caseId);
        result.setCreationDate(creationDate);
        result.setClosedDate(closedDate);

        return result;
    }
}
