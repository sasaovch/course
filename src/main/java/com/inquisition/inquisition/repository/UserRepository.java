package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.user.UserRecordMapper;
import com.inquisition.inquisition.mapper.user.UserRecordUnmapper;
import com.inquisition.inquisition.model.user.User;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.USER_TABLE;

@Repository
public class UserRepository {
    private final DSLContext dsl;
    private final UserRecordUnmapper userRecordUnmapper;
    private final UserRecordMapper userRecordMapper;

    @Autowired
    public UserRepository(DSLContext dsl,
                          UserRecordUnmapper userRecordUnmapper,
                          UserRecordMapper userRecordMapper) {
        this.dsl = dsl;
        this.userRecordUnmapper = userRecordUnmapper;
        this.userRecordMapper = userRecordMapper;
    }

    @Transactional
    public User insert(User user) {
        return dsl.insertInto(USER_TABLE)
                .set(dsl.newRecord(USER_TABLE, user))
                .returning()
                .fetchOptional()
                .map(userRecordMapper::mapUser)
                .orElse(null);
    }

    @Transactional
    public User update(User user) {
        return dsl.update(USER_TABLE)
                .set(userRecordUnmapper.unmap(user))
                .where(USER_TABLE.ID.eq(user.getId()))
                .returning()
                .fetchOptional()
                .map(userRecordMapper::mapUser)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public User find(Integer id) {
        return dsl.selectFrom(USER_TABLE)
                .where(USER_TABLE.ID.eq(id))
                .fetchOptional()
                .map(userRecordMapper::mapUser)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByPerson(Integer personId) {
        return dsl.selectFrom(USER_TABLE)
                .where(USER_TABLE.PERSON_ID.eq(personId))
                .fetchOptional()
                .map(userRecordMapper::mapUser)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> findAll(Condition condition) {
        return dsl.selectFrom(USER_TABLE)
                .where(condition)
                .fetch()
                .map(userRecordMapper::mapUser);
    }
    @Transactional()
    public Boolean delete(Integer id) {
        return dsl.deleteFrom(USER_TABLE)
                .where(USER_TABLE.ID.eq(id))
                .execute() == 1;
    }
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return dsl.selectFrom(USER_TABLE)
                .where(USER_TABLE.USERNAME.eq(username))
                .fetchOptional()
                .map(userRecordMapper::mapUser)
                .orElse(null);
    }
    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return !dsl.selectFrom(USER_TABLE)
                .where(USER_TABLE.USERNAME.eq(username))
                .fetch()
                .map(userRecordMapper::mapUser)
                .isEmpty();
    }
}
