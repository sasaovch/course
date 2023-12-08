package com.inquisition.inquisition.service.impl;

import com.inquisition.inquisition.jwt.JwtUtils;
import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithUser;
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
import com.inquisition.inquisition.service.intr.AuthenticationService;
import com.inquisition.inquisition.utils.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.repository.PersonRepository.allFieldsExceptId;

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
        return new PayloadWithUser(200, "", login(loginUser.getUsername(), loginUser.getPassword()));
    }

    public Payload registerUser(SignupUser signupUser) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupUser.getUsername()))) {
            return new BasePayload(400, "User with username already exists.");
        }

        Locality locality = localityRepository.findByName(signupUser.getLocality());
        if (locality == null) {
            return new BasePayload(400, "Locality not found.");
        }

        Person person = personRepository.findByCondition(
                allFieldsExceptId(
                        signupUser.getName(),
                        signupUser.getSurname(),
                        signupUser.getBirthDate(),
                        signupUser.getPersonGender(),
                        locality.getId()
                )
        );
        if (person == null){
            return new BasePayload(400, "User with name not found.");
        }

        Official official = officialRepository.findByPersonId(person.getId());
        UserRole role = getRoleByOfficial(official);

        User user = new User(
                person.getId(),
                signupUser.getUsername(),
                encoder.encode(signupUser.getPassword()),
                role
        );
        userRepository.insert(user);

        return new PayloadWithUser(200, "", login(user.getUsername(), signupUser.getPassword()));
    }

    private UserRole getRoleByOfficial(Official official) {
        if (official == null) return UserRole.USER;
        switch (official.getOfficialName()) {
            case BISHOP -> {
                return UserRole.BISHOP;
            }
            case FISCAL -> {
                return UserRole.FISCAL;
            }
            case INQUISITOR -> {
                return UserRole.INQUISITOR;
            }
            case SECULAR_AUTHORITY -> {
                return UserRole.SECULAR_AUTHORITY;
            }
            default -> {
                return UserRole.USER;
            }
        }
    }

    private UserDTO login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return UserConverter.convertToUserDTO(userDetails, jwt);
    }
}
