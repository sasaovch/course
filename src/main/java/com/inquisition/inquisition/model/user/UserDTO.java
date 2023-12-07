package com.inquisition.inquisition.model.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDTO {
    @NotNull
    private String username;
    @NotNull
    private UserRole role;
    private String jwtToken;

    public UserDTO(String username, UserRole role, String jwtToken) {
        this.username = username;
        this.role = role;
        this.jwtToken = jwtToken;
    }

    public UserDTO(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }
}
