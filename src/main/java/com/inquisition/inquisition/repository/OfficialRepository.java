package com.inquisition.inquisition.repository;

import com.inquisition.inquisition.mapper.official.OfficialRecordMapper;
import com.inquisition.inquisition.model.official.entity.Official;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.OFFICIAL_TABLE;

@Repository
public class OfficialRepository {
    private final DSLContext dsl;
    private final OfficialRecordMapper officialRecordMapper;

    @Autowired
    public OfficialRepository(DSLContext dsl, OfficialRecordMapper officialRecordMapper) {
        this.dsl = dsl;
        this.officialRecordMapper = officialRecordMapper;
    }

    @Transactional(readOnly = true)
    public Official getCurrentByPersonId(Integer personId) {
        return dsl.selectFrom(OFFICIAL_TABLE)
                .where(OFFICIAL_TABLE.PERSON_ID.eq(personId))
                .and(OFFICIAL_TABLE.FIRED_DATE.isNull())
                .fetchOptional()
                .map(officialRecordMapper::mapOfficial)
                .orElse(null);
    }
}
