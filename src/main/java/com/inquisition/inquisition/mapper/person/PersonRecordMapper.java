package com.inquisition.inquisition.mapper.person;

import com.inquisition.inquisition.model.person.Gender;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.models.tables.records.PersonRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.RecordMapper;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PersonRecordMapper implements RecordMapper<PersonRecord, Person> {
    private static final TableField<PersonRecord, com.inquisition.inquisition.models.enums.Gender> GENDER_TABLE_FIELD =
            com.inquisition.inquisition.models.tables.Person.PERSON.PERSON_GENDER;
    @Override
    public Person map(PersonRecord personRecord) {
        if (personRecord == null) {
            return null;
        }
        Person person = personRecord.into(Person.class);
        person.setGender(convert(personRecord.get(GENDER_TABLE_FIELD)));
        return person;
    }

    private Gender convert(com.inquisition.inquisition.models.enums.Gender modelName) {
        return Gender.valueOf(modelName.name().toUpperCase());
    }
}
