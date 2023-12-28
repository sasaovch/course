package com.inquisition.inquisition.mapper.bible;

import com.inquisition.inquisition.model.bible.entity.Bible;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.BIBLE_TABLE;

@Component
public class BibleRecordMapper {
    public Bible mapBible(Record record) {
        return mapBible(record, BIBLE_TABLE);
    }

    public Bible mapBible(Record record, com.inquisition.inquisition.models.tables.Bible alias) {
        Bible bible = new Bible();
        bible.setVersion(record.get(alias.VERSION));
        bible.setPublicationDate(record.get(alias.PUBLICATION_DATE));
        bible.setName(record.get(alias.NAME));
        return bible;
    }
}
