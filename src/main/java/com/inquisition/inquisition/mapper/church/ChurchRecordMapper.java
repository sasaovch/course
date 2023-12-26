package com.inquisition.inquisition.mapper.church;

import com.inquisition.inquisition.model.church.Church;
import com.inquisition.inquisition.model.locality.Locality;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.CHURCH_TABLE;

@Component
public class ChurchRecordMapper {

    public Church mapChurch(Record record) {
        return mapChurch(record, CHURCH_TABLE);
    }

    public Church mapChurch(Record record, com.inquisition.inquisition.models.tables.Church alias) {
        Church church = new Church();
        church.setId(record.get(alias.ID));
        church.setName(record.get(alias.NAME));
        church.setFoundationDate(record.get(alias.FOUNDATION_DATE));
        Locality locality = new Locality();
        locality.setId(record.get(alias.LOCALITY_ID));
        church.setLocality(locality);

        return church;
    }
}
