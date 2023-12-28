package com.inquisition.inquisition.repository;

import com.inquisition.inquisition.model.prison.entity.Prison;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.PRISON_TABLE;

@Repository
public class PrisonRepository {
    @Autowired
    private  DSLContext dsl;

    @Transactional(readOnly = true)
    public Prison find(Integer id) {
        return dsl.selectFrom(PRISON_TABLE)
                .where(PRISON_TABLE.ID.eq(id))
                .fetchOptional()
                .map(r -> r.into(Prison.class))
                .orElse(null);
    }
}
