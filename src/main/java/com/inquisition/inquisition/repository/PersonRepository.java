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

import static com.inquisition.inquisition.utils.TableAliases.PERSON_TABLE;

@Repository
public class PersonRepository {
    private final DSLContext dsl;
    private final PersonRecordMapper personRecordMapper;

    @Autowired
    public PersonRepository(DSLContext dsl, PersonRecordMapper personRecordMapper) {
        this.dsl = dsl;
        this.personRecordMapper = personRecordMapper;
    }

    @Transactional(readOnly = true)
    public Person find(Integer id) {
        return dsl.selectFrom(PERSON_TABLE)
                .where(PERSON_TABLE.ID.eq(id))
                .fetchOptional()
                .map(personRecordMapper::mapPerson)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Person findByCondition(Condition condition) {
        return dsl.selectFrom(PERSON_TABLE)
                .where(condition)
                .fetchOptional()
                .map(personRecordMapper::mapPerson)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Person> findAll(Condition condition) {
        return dsl.selectFrom(PERSON_TABLE)
                .where(condition)
                .fetch()
                .map(personRecordMapper::mapPerson);
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
                .condition(PERSON_TABLE.NAME.eq(name))
                .and(PERSON_TABLE.SURNAME.eq(surname))
                .and(PERSON_TABLE.BIRTH_DATE.eq(birthDate))
                .and(PERSON_TABLE.PERSON_GENDER.eq(genderModels))
                .and(PERSON_TABLE.LOCALITY_ID.eq(localityId));
    }

    public static Condition byLocalityId(Integer localityId) {
        return DSL.condition(PERSON_TABLE.LOCALITY_ID.eq(localityId));
    }
}
