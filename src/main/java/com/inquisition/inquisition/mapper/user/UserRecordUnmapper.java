package com.inquisition.inquisition.mapper.user;

import com.inquisition.inquisition.model.user.entity.User;
import com.inquisition.inquisition.model.user.entity.UserRole;
import com.inquisition.inquisition.models.enums.UserRoles;
import com.inquisition.inquisition.models.tables.Users;
import com.inquisition.inquisition.models.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.jooq.exception.MappingException;
import org.springframework.stereotype.Component;

@Component
public class UserRecordUnmapper {
    private final DSLContext dsl;

    public UserRecordUnmapper(DSLContext dsl) {
        this.dsl = dsl;
    }

    public UsersRecord unmap(User user) throws MappingException {
        UsersRecord record = dsl.newRecord(Users.USERS, user);
        record.setRole(convert(user.getRole()));
        return record;
    }

    private UserRoles convert(UserRole role) {
        return UserRoles.valueOf(role.name().toUpperCase());
    }
}
