package com.inquisition.inquisition.mapper.official;

import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.official.OfficialName;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.OFFICIAL_TABLE;


@Component
public class OfficialRecordMapper {

    public Official mapOfficial(Record record) {
        return mapOfficial(record, OFFICIAL_TABLE);
    }


    private OfficialName convertOfficialName(com.inquisition.inquisition.models.enums.OfficialName modelName) {
        for (OfficialName value : OfficialName.values()) {
            if (value.getDisplayName().equals(modelName.name())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid MyEnum id: " + modelName.name());
    }

    public Official mapOfficial(Record record, com.inquisition.inquisition.models.tables.Official alias) {
        Official official = new Official();
        official.setId(record.get(alias.ID));
        official.setPersonId(record.get(alias.PERSON_ID));
        official.setOfficialName(convertOfficialName(record.get(alias.OFFICIAL_NAME)));
        official.setEmploymentDate(record.get(alias.EMPLOYMENT_DATE));
        official.setFiredDate(record.get(alias.FIRED_DATE));

        return official;
    }
}

