package com.inquisition.inquisition.mapper.person;

import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.models.enums.Gender;
import com.inquisition.inquisition.models.tables.records.PersonRecord;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonRecordUnmapper implements RecordUnmapper<Person, PersonRecord> {
    @Autowired
    private final DSLContext dsl;

    @Override
    public @NotNull PersonRecord unmap(Person person) throws MappingException {
        PersonRecord record = dsl.newRecord(com.inquisition.inquisition.models.tables.Person.PERSON, person);
        record.setPersonGender(convert(person.getGender()));
        return record;
    }

    private Gender convert(com.inquisition.inquisition.model.person.Gender gender) {
        return Gender.valueOf(gender.name().toUpperCase());
    }
}
