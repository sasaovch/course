package com.inquisition.inquisition.mapper.user;

import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.models.enums.UserRoles;
import com.inquisition.inquisition.models.tables.Users;
import com.inquisition.inquisition.models.tables.records.UsersRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRecordUnmapper implements RecordUnmapper<User, UsersRecord> {
    @Autowired
    private final DSLContext dsl;
    @Override
    public @NonNull UsersRecord unmap(User user) throws MappingException {
        UsersRecord record = dsl.newRecord(Users.USERS, user);
        record.setRole(convert(user.getRole()));
        return record;
    }

    private UserRoles convert(UserRole role) {
        return UserRoles.valueOf(role.name().toUpperCase());
    }
}
