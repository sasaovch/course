package com.inquisition.inquisition.model.user.payload;

import com.inquisition.inquisition.model.user.entity.UserRole;

public record UserPayload(
        String username,
        UserRole role,
        String jwt,
        Integer personId,
        Integer officialId,
        String personName,
        Integer localityId
) {
}
