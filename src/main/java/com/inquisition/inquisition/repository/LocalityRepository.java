package com.inquisition.inquisition.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import com.inquisition.inquisition.model.locality.Locality;
//import com.inquisition.inquisition.model.user.User;
//import org.springframework.data.jpa.repository.JpaRepository;

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

//
@Repository
public class LocalityRepository implements CrudRepository<Locality> {
    private final DSLContext dsl;
    private final com.inquisition.inquisition.models.tables.Locality LOCALITY;

    @Autowired
    public LocalityRepository(DSLContext dsl) {
        this.dsl = dsl;
        LOCALITY = com.inquisition.inquisition.models.tables.Locality.LOCALITY;
    }

    @Override
    public Locality insert(Locality locality) {
        return dsl.insertInto(LOCALITY)
                .set(dsl.newRecord(LOCALITY, locality))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error inserting entity: " + locality.getId()))
                .into(Locality.class);
    }

    @Override
    public Locality update(Locality locality) {
        return null;
    }

    @Override
    public Locality find(Integer id) {
        return null;
    }

    public Locality findByName(String name) {
        Optional<LocalityRecord> res = dsl.selectFrom(LOCALITY)
                .where(LOCALITY.NAME.eq(name))
                .fetchOptional();
        var result = res.map(localityRecord -> {
            var r = localityRecord.into(Locality.class);
            return r;
        }).orElse(null);
        return result;
    }

    @Override
    public List<Locality> findAll(Condition condition) {
        return dsl.selectFrom(LOCALITY)
                .fetch()
                .map(r -> r.into(Locality.class));
    }

    public List<Locality> findAll() {
        return findAll(DSL.trueCondition());
    }

    @Override
    public Boolean delete(Integer id) {
        return null;
    }
}
