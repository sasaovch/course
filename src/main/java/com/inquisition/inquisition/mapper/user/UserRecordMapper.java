package com.inquisition.inquisition.mapper.user;

import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.models.enums.UserRoles;
import com.inquisition.inquisition.models.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRecordMapper implements RecordMapper<UsersRecord, User> {

    @Override
    public User map(UsersRecord record) {
        return new User(
                record.getPersonId(),
                record.getUsername(),
                record.getPassword(),
                convert(record.getRole())
        );
    }

    private UserRole convert(UserRoles role) {
        return UserRole.valueOf(role.name().toUpperCase());
    }
}
