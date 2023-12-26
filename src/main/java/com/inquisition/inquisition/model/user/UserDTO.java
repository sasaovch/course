package com.inquisition.inquisition.model.user;

public record UserDTO(
        String username,
        UserRole role,
        String jwt,
        Integer personId,
        Integer officialId,
        String personName,
        Integer localityId
) {
}
