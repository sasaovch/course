package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.bible.Commandment;
import com.inquisition.inquisition.models.tables.BibleCommandment;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//FIXME: good
@Repository
public class BibleRepository {
    private final DSLContext dsl;
    private static final com.inquisition.inquisition.models.tables.Commandment COMMANDMENT =
            com.inquisition.inquisition.models.tables.Commandment.COMMANDMENT;

    @Autowired
    public BibleRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional(readOnly = true)
    public List<Bible> findAll() {
        return findAll(DSL.trueCondition());
    }

    @Transactional(readOnly = true)
    public List<Bible> findAll(Condition condition) {
        return dsl.selectFrom(com.inquisition.inquisition.models.tables.Bible.BIBLE)
                .where(condition)
                .fetch()
                .map(r -> r.into(Bible.class));
    }

    @Transactional(readOnly = true)
    public List<Commandment> getCommandment(Integer bibleId) {
        return dsl.select()
                .from(COMMANDMENT)
                .join(BibleCommandment.BIBLE_COMMANDMENT)
                .on(BibleCommandment.BIBLE_COMMANDMENT.COMMANDMENT_ID.eq(COMMANDMENT.ID))
                .where(BibleCommandment.BIBLE_COMMANDMENT.BIBLE_ID.eq(bibleId))
                .fetch()
                .map(r -> r.into(Commandment.class));
    }
}
