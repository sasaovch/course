package com.inquisition.inquisition.model.user.entity;

import com.inquisition.inquisition.model.user.entity.UserRole;

public record LoginedUser(
        String username,
        UserRole role,
        String jwtToken
) {}
