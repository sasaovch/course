package com.inquisition.inquisition.mapper.locality;

import com.inquisition.inquisition.model.locality.Locality;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.LOCALITY_TABLE;

@Component
public class LocalityRecordMapper {

    public Locality mapLocality(Record record) {
        return mapLocality(record, LOCALITY_TABLE);
    }

    public Locality mapLocality(Record record, com.inquisition.inquisition.models.tables.Locality alias) {
        Locality locality = new Locality();
        locality.setId(record.get(alias.ID));
        locality.setName(record.get(alias.NAME));
        locality.setFoundationDate(record.get(alias.FOUNDATION_DATE));

        return locality;
    }
}
