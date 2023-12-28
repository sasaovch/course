package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.locality.LocalityRecordMapper;
import com.inquisition.inquisition.model.locality.entity.Locality;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.LOCALITY_TABLE;

@Repository
public class LocalityRepository {
    private final DSLContext dsl;
    private final LocalityRecordMapper localityRecordMapper;

    @Autowired
    public LocalityRepository(DSLContext dsl, LocalityRecordMapper localityRecordMapper) {
        this.dsl = dsl;
        this.localityRecordMapper = localityRecordMapper;
    }

    @Transactional(readOnly = true)
    public Locality find(Integer id) {
        return dsl.selectFrom(LOCALITY_TABLE)
                .where(LOCALITY_TABLE.ID.eq(id))
                .fetchOptional()
                .map(localityRecordMapper::mapLocality)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Locality> findAllByCondition(Condition condition) {
        return dsl.selectFrom(LOCALITY_TABLE)
                .fetch()
                .map(localityRecordMapper::mapLocality);
    }

    @Transactional(readOnly = true)
    public List<Locality> findAllByCondition() {
        return findAllByCondition(DSL.trueCondition());
    }
}
