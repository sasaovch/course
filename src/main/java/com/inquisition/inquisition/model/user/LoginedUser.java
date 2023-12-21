package com.inquisition.inquisition.model.user;

public record LoginedUser(
        String username,
        UserRole role,
        String jwtToken
) {}
//@Data
//@RequiredArgsConstructor
//public class UserDTO {
//    @NotNull
//    private String username;
//    @NotNull
//    private UserRole role;
//    private String jwtToken;
//
//    public UserDTO(String username, UserRole role, String jwtToken) {
//        this.username = username;
//        this.role = role;
//        this.jwtToken = jwtToken;
//    }
//
//    public UserDTO(String username, UserRole role) {
//        this.username = username;
//        this.role = role;
//    }
//}
