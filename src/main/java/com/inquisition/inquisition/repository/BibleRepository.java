package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.bible.BibleRecordMapper;
import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.bible.Commandment;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.BIBLE_COMMANDMENT_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.BIBLE_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.COMMANDMENT_TABLE;

@Repository
public class BibleRepository {
    private final DSLContext dsl;
    private final BibleRecordMapper bibleRecordMapper;

    @Autowired
    public BibleRepository(DSLContext dsl, BibleRecordMapper bibleRecordMapper) {
        this.dsl = dsl;
        this.bibleRecordMapper = bibleRecordMapper;
    }

    @Transactional(readOnly = true)
    public List<Bible> findAll() {
        return findAll(DSL.trueCondition());
    }

    @Transactional(readOnly = true)
    public List<Bible> findAll(Condition condition) {
        return dsl.selectFrom(BIBLE_TABLE)
                .where(condition)
                .fetch()
                .map(bibleRecordMapper::mapBible);
    }

    @Transactional(readOnly = true)
    public Bible find(Integer version) {
        return dsl.selectFrom(BIBLE_TABLE)
                .where(BIBLE_TABLE.VERSION.eq(version))
                .fetchOptional()
                .map(bibleRecordMapper::mapBible)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Commandment> getCommandment(Integer bibleId) {
        return dsl.select()
                .from(COMMANDMENT_TABLE)
                .join(BIBLE_COMMANDMENT_TABLE)
                .on(BIBLE_COMMANDMENT_TABLE.COMMANDMENT_ID.eq(COMMANDMENT_TABLE.ID))
                .where(BIBLE_COMMANDMENT_TABLE.BIBLE_ID.eq(bibleId))
                .fetch()
                .map(r -> r.into(Commandment.class));
    }
}
