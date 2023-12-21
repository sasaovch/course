package com.inquisition.inquisition.repository;

import java.time.LocalDate;
import java.util.List;

import com.inquisition.inquisition.mapper.person.PersonRecordMapper;
import com.inquisition.inquisition.model.person.Gender;
import com.inquisition.inquisition.model.person.Person;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PersonRepository {
    private final DSLContext dsl;
    private final PersonRecordMapper personRecordMapper;
    private static final com.inquisition.inquisition.models.tables.Person PERSON =
            com.inquisition.inquisition.models.tables.Person.PERSON;

    @Autowired
    public PersonRepository(DSLContext dsl, PersonRecordMapper personRecordMapper) {
        this.dsl = dsl;
        this.personRecordMapper = personRecordMapper;
    }

    @Transactional(readOnly = true)
    public Person find(Integer id) {
        return dsl.selectFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .fetchOptional()
                .map(personRecordMapper::map)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Person findByCondition(Condition condition) {
        return dsl.selectFrom(PERSON)
                .where(condition)
                .fetchOptional()
                .map(personRecordMapper::map)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Person> findAll(Condition condition) {
        return dsl.selectFrom(PERSON)
                .where(condition)
                .fetch()
                .map(personRecordMapper::map);
    }

    public static Condition allFieldsExceptId(
            String name,
            String surname,
            LocalDate birthDate,
            Gender gender,
            Integer localityId
    ) {
        com.inquisition.inquisition.models.enums.Gender genderModels =
                com.inquisition.inquisition.models.enums.Gender.valueOf(gender.toString().toUpperCase());
        return DSL
                .condition(PERSON.NAME.eq(name))
                .and(PERSON.SURNAME.eq(surname))
                .and(PERSON.BIRTH_DATE.eq(birthDate))
                .and(PERSON.PERSON_GENDER.eq(genderModels))
                .and(PERSON.LOCALITY_ID.eq(localityId));
    }

    public static Condition byLocalityId(Integer localityId) {
        return DSL.condition(PERSON.LOCALITY_ID.eq(localityId));
    }
}
