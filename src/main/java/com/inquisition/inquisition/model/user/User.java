package com.inquisition.inquisition.model.user;

import com.inquisition.inquisition.model.person.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
//@Entity(name = "users")
public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    @OneToOne
//    @JoinColumn(name = "person_id")
    @NonNull
    private Integer personId;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private UserRole role;
//    @Transient
    private String jwtToken;

//    public User(Integer id, String username, String password, UserRole role, String jwtToken) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.role = role;
//        this.jwtToken = jwtToken;
//    }
//
//    public User(String username, String password, UserRole role) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }
}
