package com.inquisition.inquisition.model.user;

public record LoginedUser(
        String username,
        UserRole role,
        String jwtToken
) {}
