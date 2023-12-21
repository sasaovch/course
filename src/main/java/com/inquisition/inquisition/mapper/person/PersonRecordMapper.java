package com.inquisition.inquisition.mapper.person;

import com.inquisition.inquisition.model.person.Gender;
import com.inquisition.inquisition.model.person.Person;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.PERSON_TABLE;

@Component
public class PersonRecordMapper {

    public Person mapPerson(Record record) {
        return mapPerson(record, PERSON_TABLE);
    }

    public Person mapPerson(Record record, com.inquisition.inquisition.models.tables.Person alias) {
        return new Person(
                record.get(alias.ID),
                record.get(alias.NAME),
                record.get(alias.SURNAME),
                record.get(alias.BIRTH_DATE),
                convertGender(record.get(alias.PERSON_GENDER)),
                record.get(alias.LOCALITY_ID)
        );
    }

    public Gender convertGender(com.inquisition.inquisition.models.enums.Gender modelName) {
        return Gender.valueOf(modelName.name().toUpperCase());
    }
}
