package com.inquisition.inquisition.mapper.official;

import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.official.OfficialName;
import com.inquisition.inquisition.models.tables.records.OfficialRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.RecordMapper;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OfficialRecordMapper implements RecordMapper<OfficialRecord, Official> {
    private static final TableField<OfficialRecord, com.inquisition.inquisition.models.enums.OfficialName> OFFICIAL_NAME_TABLE_FIELD =
            com.inquisition.inquisition.models.tables.Official.OFFICIAL.OFFICIAL_NAME;
    @Override
    public Official map(OfficialRecord officialRecord) {
        if (officialRecord == null) return null;
        Official official = officialRecord.into(Official.class);
        official.setOfficialName(convert(officialRecord.get(OFFICIAL_NAME_TABLE_FIELD)));
        return official;
    }

    private OfficialName convert(com.inquisition.inquisition.models.enums.OfficialName modelName) {
        return OfficialName.valueOf(modelName.name().toUpperCase());
    }
}

