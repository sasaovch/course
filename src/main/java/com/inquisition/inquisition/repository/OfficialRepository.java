package com.inquisition.inquisition.repository;
//
//import java.util.Optional;
//
//import com.inquisition.inquisition.model.official.Official;
//import com.inquisition.inquisition.model.person.Person;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//

import java.util.List;

import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.mapper.official.OfficialRecordMapper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OfficialRepository implements CrudRepository<Official> {
    private final DSLContext dsl;
    private final OfficialRecordMapper officialRecordMapper;
    private static final com.inquisition.inquisition.models.tables.Official OFFICIAL =
            com.inquisition.inquisition.models.tables.Official.OFFICIAL;

    @Autowired
    public OfficialRepository(DSLContext dsl, OfficialRecordMapper officialRecordMapper) {
        this.dsl = dsl;
        this.officialRecordMapper = officialRecordMapper;
    }

    @Override
    public Official insert(Official official) {
        return null;
    }

    @Override
    public Official update(Official official) {
        return null;
    }

    @Override
    public Official find(Integer id) {
        return null;
    }

    public Official findByPersonId(Integer personId) {
        return dsl.selectFrom(OFFICIAL)
                .where(OFFICIAL.PERSON_ID.eq(personId))
                .fetchOptional()
                .map(officialRecordMapper::map)
                .orElse(null);
    }

    @Override
    public List<Official> findAll(Condition condition) {
        return null;
    }

    @Override
    public Boolean delete(Integer id) {
        return null;
    }
}
//    Optional<Official> findByPersonId(Integer personId);
//}
