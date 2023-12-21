package com.inquisition.inquisition.mapper.bible;

import com.inquisition.inquisition.model.bible.Bible;
import org.jooq.Record;

public class BibleRecordMapper {
    public static Bible map(Record record) {
        Bible bible = new Bible();
        bible.setVersion(record.get(com.inquisition.inquisition.models.tables.Bible.BIBLE.VERSION));
        bible.setPublicationDate(record.get(com.inquisition.inquisition.models.tables.Bible.BIBLE.PUBLICATION_DATE));
        bible.setName(record.get(com.inquisition.inquisition.models.tables.Bible.BIBLE.NAME));
        return bible;
    }

    public static Bible mapWithAlias(String alias, Record record) {
        Bible bible = new Bible();
        bible.setVersion(record.get(com.inquisition.inquisition.models.tables.Bible.BIBLE.as(alias).VERSION));
        bible.setPublicationDate(record.get(com.inquisition.inquisition.models.tables.Bible.BIBLE.as(alias).PUBLICATION_DATE));
        bible.setName(record.get(com.inquisition.inquisition.models.tables.Bible.BIBLE.as(alias).NAME));
        return bible;
    }
}
