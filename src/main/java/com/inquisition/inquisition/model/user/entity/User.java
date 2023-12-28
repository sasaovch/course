package com.inquisition.inquisition.model.user.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private Integer personId;
    private String username;
    private String password;
    private UserRole role;
    private String jwtToken;
}
