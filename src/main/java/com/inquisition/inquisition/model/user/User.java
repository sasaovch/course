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
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private UserRole role;
    @Transient
    private String jwtToken;

    public User(Long id, String username, String password, UserRole role, String jwtToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.jwtToken = jwtToken;
    }

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
