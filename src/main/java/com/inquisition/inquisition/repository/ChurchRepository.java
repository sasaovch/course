package com.inquisition.inquisition.repository;

import com.inquisition.inquisition.mapper.church.ChurchRecordMapper;
import com.inquisition.inquisition.model.church.Church;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.CHURCH_TABLE;

@Repository
public class ChurchRepository {
    @Autowired
    private DSLContext dsl;
    @Autowired
    private ChurchRecordMapper churchRecordMapper;

    @Transactional(readOnly = true)
    public Church findByLocality(Integer localityId) {
        return dsl.selectFrom(CHURCH_TABLE)
                .where(CHURCH_TABLE.LOCALITY_ID.eq(localityId))
                .fetchOptional()
                .map(churchRecordMapper::mapChurch)
                .orElse(null);
    }
}
