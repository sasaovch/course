package com.inquisition.inquisition.repository;

import java.time.LocalDate;
import java.util.List;

import com.inquisition.inquisition.mapper.person.PersonRecordUnmapper;
import com.inquisition.inquisition.model.person.Gender;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.mapper.person.PersonRecordMapper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository implements CrudRepository<Person> {
    private final DSLContext dsl;
    private final PersonRecordMapper personRecordMapper;
    private final PersonRecordUnmapper personRecordUnmapper;
    private static final com.inquisition.inquisition.models.tables.Person PERSON =
            com.inquisition.inquisition.models.tables.Person.PERSON;

    @Autowired
    public PersonRepository(DSLContext dsl, PersonRecordMapper personRecordMapper, PersonRecordUnmapper personRecordUnmapper) {
        this.dsl = dsl;
        this.personRecordMapper = personRecordMapper;
        this.personRecordUnmapper = personRecordUnmapper;
    }


    @Override
    public Person insert(Person person) {
        return null;
    }

    @Override
    public Person update(Person person) {
        return null;
    }

    @Override
    public Person find(Integer id) {
        return null;
    }

    public Person findByCondition(Condition condition) {
        return dsl.selectFrom(PERSON)
                .where(condition)
                .fetchOptional()
                .map(personRecordMapper::map)
                .orElse(null);
    }

    @Override
    public List<Person> findAll(Condition condition) {
        return dsl.selectFrom(PERSON)
                .where(condition)
                .fetch()
                .map(personRecordMapper::map);
    }

    @Override
    public Boolean delete(Integer id) {
        return null;
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

    public static Condition byLocalityId(
            Integer localityId
    ) {
        return DSL
                .condition(PERSON.LOCALITY_ID.eq(localityId));
    }
}
//    Optional<Person> findByNameAndSurnameAndBirthDateAndGenderAndLocalityId(
//            String name, String surname, LocalDate birthDate, Gender gender, Integer localityId
//    );
//}
