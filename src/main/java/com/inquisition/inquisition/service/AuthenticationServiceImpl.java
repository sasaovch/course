package com.inquisition.inquisition.service;

import java.time.LocalDate;
import java.util.Optional;

import com.inquisition.inquisition.jwt.JwtUtils;
import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.message.MessageResponse;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.official.OfficialName;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithData;
import com.inquisition.inquisition.model.payload.PayloadWithUser;
import com.inquisition.inquisition.model.person.Gender;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.model.user.LoginUser;
import com.inquisition.inquisition.model.user.SignupUser;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.UserDTO;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.repository.LocalityRepository;
import com.inquisition.inquisition.repository.OfficialRepository;
import com.inquisition.inquisition.repository.PersonRepository;
import com.inquisition.inquisition.repository.UserRepository;
import com.inquisition.inquisition.security.UserDetailsImpl;
import com.inquisition.inquisition.utils.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @Autowired
    private LocalityRepository localityRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OfficialRepository officialRepository;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }
    public Payload authenticateUser(LoginUser loginUser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new PayloadWithUser(200, "", UserConverter.convertToUserDTO(userDetails, jwt));
    }

    public Payload registerUser(SignupUser signupUser) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupUser.getUsername()))) {
            return new BasePayload(400, "User with username already exists.");
        }

        Optional<Locality> optLocality = localityRepository.findByName(signupUser.getLocality());
        if (optLocality.isEmpty()) {
            return new BasePayload(400, "Locality not found.");
        }
        Locality locality = optLocality.get();

        Optional<Person> optPerson = personRepository.findByNameAndSurnameAndBirthDateAndGenderAndLocalityId(
                signupUser.getName(), signupUser.getSurname(), signupUser.getBirthDate(), signupUser.getGender(), locality.getId()
        );
        if (optPerson.isEmpty()){
            return new BasePayload(400, "User with name not found.");
        }
        Person person = optPerson.get();

        Optional<Official> optOfficial = officialRepository.findByPersonId(person.getId());
        Official official = optOfficial.orElse(null);
        UserRole role = getRoleByOfficial(official);

        User user = new User(
                signupUser.getUsername(),
                encoder.encode(signupUser.getPassword()),
                role
        );
        user.setPerson(person);
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupUser.getUsername(), signupUser.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new PayloadWithUser(200, "", UserConverter.convertToUserDTO(userDetails, jwt));
    }

    private UserRole getRoleByOfficial(Official official) {
        if (official == null) return UserRole.User;
        switch (official.getOfficialName()) {
            case Bishop -> {
                return UserRole.Bishop;
            }
            case Fiscal -> {
                return UserRole.Fiscal;
            }
            case Inquisitor -> {
                return UserRole.Inquisitor;
            }
            case SecularAuthority -> {
                return UserRole.SecularAuthority;
            }
            default -> {
                return UserRole.User;
            }
        }
    }
}
