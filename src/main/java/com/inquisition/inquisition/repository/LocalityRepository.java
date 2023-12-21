package com.inquisition.inquisition.repository;

import java.util.List;
import java.util.Optional;

import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.models.tables.records.LocalityRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LocalityRepository {
    private final DSLContext dsl;
    private final com.inquisition.inquisition.models.tables.Locality LOCALITY;

    @Autowired
    public LocalityRepository(DSLContext dsl) {
        this.dsl = dsl;
        LOCALITY = com.inquisition.inquisition.models.tables.Locality.LOCALITY;
    }

//    public Locality insert(Locality locality) {
//        return dsl.insertInto(LOCALITY)
//                .set(dsl.newRecord(LOCALITY, locality))
//                .returning()
//                .fetchOptional()
//                .orElseThrow(() -> new DataAccessException("Error inserting entity: " + locality.getId()))
//                .into(Locality.class);
//    }

    @Transactional(readOnly = true)
    public Locality findByName(String name) {
        Optional<LocalityRecord> res = dsl.selectFrom(LOCALITY)
                .where(LOCALITY.NAME.eq(name))
                .fetchOptional();
        return res
                .map(localityRecord -> localityRecord.into(Locality.class))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Locality> findAll(Condition condition) {
        return dsl.selectFrom(LOCALITY)
                .fetch()
                .map(r -> r.into(Locality.class));
    }

    @Transactional(readOnly = true)
    public List<Locality> findAll() {
        return findAll(DSL.trueCondition());
    }
}
