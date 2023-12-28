package com.inquisition.inquisition.service;

import com.inquisition.inquisition.jwt.JwtUtils;
import com.inquisition.inquisition.model.locality.entity.Locality;
import com.inquisition.inquisition.model.official.entity.Official;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithUser;
import com.inquisition.inquisition.model.person.entity.Person;
import com.inquisition.inquisition.model.user.container.LoginUser;
import com.inquisition.inquisition.model.user.entity.LoginedUser;
import com.inquisition.inquisition.model.user.container.SignupUser;
import com.inquisition.inquisition.model.user.entity.User;
import com.inquisition.inquisition.model.user.payload.UserPayload;
import com.inquisition.inquisition.model.user.entity.UserRole;
import com.inquisition.inquisition.repository.LocalityRepository;
import com.inquisition.inquisition.repository.OfficialRepository;
import com.inquisition.inquisition.repository.PersonRepository;
import com.inquisition.inquisition.repository.UserRepository;
import com.inquisition.inquisition.security.UserDetailsImpl;
import com.inquisition.inquisition.utils.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.repository.PersonRepository.allFieldsExceptId;
import static com.inquisition.inquisition.utils.UserConverter.getRoleByOfficial;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final LocalityRepository localityRepository;

    private final PersonRepository personRepository;

    private final OfficialRepository officialRepository;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository,
                                 PasswordEncoder encoder, JwtUtils jwtUtils, LocalityRepository localityRepository,
                                 PersonRepository personRepository, OfficialRepository officialRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.localityRepository = localityRepository;
        this.personRepository = personRepository;
        this.officialRepository = officialRepository;
    }

    public Payload authenticateUser(LoginUser loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername());
        if (user == null) {
            return new BasePayload(400, "Пользователь не найден.");
        }

        LoginedUser loginedUser = login(user.getUsername(), loginUser.getPassword());
        Person person = personRepository.find(user.getPersonId());
        Official official = officialRepository.getCurrentByPersonId(person.getId());
        UserRole role = getRoleByOfficial(official);
        Integer officialId = null;
        if (official != null) {
            officialId = official.getId();
        }

        UserPayload userPayload = new UserPayload(
                loginedUser.username(),
                role,
                loginedUser.jwtToken(),
                person.getId(),
                officialId,
                person.getName() + " " + person.getSurname(),
                person.getLocalityId()
        );

        return new PayloadWithUser(200, userPayload);
    }

    public Payload registerUser(SignupUser signupUser) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupUser.getUsername()))) {
            return new BasePayload(400, "Имя пользователя уже занято.");
        }

        Locality locality = localityRepository.find(signupUser.getLocality());
        if (locality == null) {
            return new BasePayload(400, "Местность не найдена.");
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
        if (person == null) {
            return new BasePayload(400, "Пользователь не найден.");
        }
        if (userRepository.findByPerson(person.getId()) != null) {
            return new BasePayload(400, "Аккаунт уже существует");
        }

        Official official = officialRepository.getCurrentByPersonId(person.getId());
        UserRole role = getRoleByOfficial(official);
        Integer officialId = null;
        if (official != null) {
            officialId = official.getId();
        }

        User user = new User();
        user.setPersonId(person.getId());
        user.setUsername(signupUser.getUsername());
        user.setPassword(encoder.encode(signupUser.getPassword()));
        user.setRole(role);

        userRepository.insert(user);
        LoginedUser loginedUser = login(user.getUsername(), signupUser.getPassword());
        UserPayload userPayload = new UserPayload(
                loginedUser.username(),
                role,
                loginedUser.jwtToken(),
                person.getId(),
                officialId,
                person.getName() + " " + person.getSurname(),
                person.getLocalityId()
        );

        return new PayloadWithUser(200, userPayload);
    }

    private LoginedUser login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return UserConverter.convertToUserDTO(userDetails, jwt);
    }
}
