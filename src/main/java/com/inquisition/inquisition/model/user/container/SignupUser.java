package com.inquisition.inquisition.model.user.container;

import java.time.LocalDate;

import com.inquisition.inquisition.model.person.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupUser {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotBlank
    @Size(min = 3, max = 20)
    private String surname;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private Gender personGender;
    @NotNull
    private Integer locality;
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
