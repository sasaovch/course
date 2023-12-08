package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.mapper.user.UserRecordMapper;
import com.inquisition.inquisition.mapper.user.UserRecordUnmapper;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.models.enums.UserRoles;
import com.inquisition.inquisition.models.tables.Users;
import com.inquisition.inquisition.models.tables.records.UsersRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements CrudRepository<User> {
    private final DSLContext dsl;
    private final UserRecordMapper userRecordMapper;
    private final UserRecordUnmapper userRecordUnmapper;
    private static final Users USER = Users.USERS;

    @Autowired
    public UserRepository(DSLContext dsl, UserRecordMapper userRecordMapper, UserRecordUnmapper userRecordUnmapper) {
        this.dsl = dsl;
        this.userRecordMapper = userRecordMapper;
        this.userRecordUnmapper = userRecordUnmapper;
    }

    public User insert(User user) {
        return dsl.insertInto(USER)
                .set(dsl.newRecord(USER, user))
                .returning()
                .fetchOptional()
                .map(userRecordMapper::map)
                .orElse(null);
    }

    public User insertValues(User user) {
//        UserRoles role = UserRoles.valueOf(user.getRole().toString().toUpperCase());
        return dsl.insertInto(USER)  //insert into countries
                .set(userRecordUnmapper.unmap(user))
                .returning()
                .fetchOne()
                .map(r -> userRecordMapper.map((UsersRecord) r));
    }

    public User update(User user) {
        return dsl.update(USER)
                .set(userRecordUnmapper.unmap(user))
                .where(USER.ID.eq(user.getId()))
                .returning()
                .fetchOptional()
                .map(userRecordMapper::map)
                .orElse(null);
    }

    public User find(Integer id) {
        return dsl.selectFrom(USER) //select * from countries
                .where(USER.ID.eq(id))  //where id = ?
                .fetchAny()  //здесь определяем, что мы хотим вернуть
                .map(r -> userRecordMapper.map((UsersRecord) r));
    }

    @Override
    public List<User> findAll(Condition condition) {
        return dsl.selectFrom(USER)
                .where(condition)
                .fetch()
                .map(userRecordMapper::map);
    }

    public Boolean delete(Integer id) {
        return dsl.deleteFrom(USER)
                .where(USER.ID.eq(id))
                .execute() == 1;
    }

    public User findByUsername(String username) {
        return dsl.selectFrom(USER)
                .where(USER.USERNAME.eq(username))
                .fetchOptional()
                .map(userRecordMapper::map)
                .orElse(null);
    }

    public Boolean existsByUsername(String username) {
        return !dsl.selectFrom(USER)
                .where(USER.USERNAME.eq(username))
                .fetch()
                .map(userRecordMapper::map)
                .isEmpty();
    }
}
