package com.inquisition.inquisition.mapper.user;

import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.models.enums.UserRoles;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.USER_TABLE;

@Component
public class UserRecordMapper {

    public User mapUser(Record record) {
        User user = new User();

        user.setId(record.get(USER_TABLE.ID));
        user.setPersonId(record.get(USER_TABLE.PERSON_ID));
        user.setUsername(record.get(USER_TABLE.USERNAME));
        user.setPassword(record.get(USER_TABLE.PASSWORD));
        user.setRole(convertRole(record.get(USER_TABLE.ROLE)));

        return user;
    }

    private static UserRole convertRole(UserRoles role) {
        return UserRole.valueOf(role.name().toUpperCase());
    }
}
