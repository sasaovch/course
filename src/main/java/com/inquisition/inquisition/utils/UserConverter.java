package com.inquisition.inquisition.utils;

import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.LoginedUser;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.security.UserDetailsImpl;

public final class UserConverter {

    public static LoginedUser convertToUserDTO(UserDetailsImpl userDetails, String jwt) {
        UserRole userRole = UserRole.valueOf(userDetails.getAuthority().getAuthority());

        return new LoginedUser(userDetails.getUsername(), userRole, jwt);
    }

    public static LoginedUser convertToUserDTO(User user) {
        return new LoginedUser(user.getUsername(), user.getRole(), "");
    }

    public static UserRole getRoleByOfficial(Official official) {
        if (official == null) return UserRole.USER;
        switch (official.getOfficialName()) {
            case BISHOP -> {
                return UserRole.BISHOP;
            }
            case FISCAL -> {
                return UserRole.FISCAL;
            }
            case INQUISITOR -> {
                return UserRole.INQUISITOR;
            }
            case SECULAR_AUTHORITY -> {
                return UserRole.SECULAR_AUTHORITY;
            }
            default -> {
                return UserRole.USER;
            }
        }
    }
}
