package com.inquisition.inquisition.utils;

import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.UserDTO;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.security.UserDetailsImpl;

public class UserConverter {

    public static UserDTO convertToUserDTO(UserDetailsImpl userDetails, String jwt) {
        UserRole userRole = UserRole.valueOf(userDetails.getAuthority().getAuthority());

        return new UserDTO(userDetails.getUsername(), userRole, jwt);
    }

    public static UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getUsername(), user.getRole(), "");
    }
}
