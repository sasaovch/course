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
import com.inquisition.inquisition.model.user.LoginedUser;
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
import static com.inquisition.inquisition.utils.UserConverter.getRoleByOfficial;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final LocalityRepository localityRepository;

    private final PersonRepository personRepository;

    private final OfficialRepository officialRepository;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
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
    //FIXME: повесить везде аннотации транзакции
    //FIXME: good
    public Payload authenticateUser(LoginUser loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername());
        if (user == null) {
            return new BasePayload(400, "User with username doesn't exist.");
        }

        LoginedUser loginedUser = login(user.getUsername(), loginUser.getPassword());
        Person person = personRepository.find(user.getPersonId());
        Official official = officialRepository.getCurrentByPersonId(person.getId());
        UserRole role = getRoleByOfficial(official);
        Integer officialId = null;
        if (official != null) {
            officialId = official.getId();
        }

        UserDTO userDTO = new UserDTO(
                loginedUser.username(),
                role,
                loginedUser.jwtToken(),
                person.getId(),
                officialId,
                person.getName() + " " + person.getSurname()
        );

        return new PayloadWithUser(200, userDTO);
    }
    //FIXME: good
    public Payload registerUser(SignupUser signupUser) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupUser.getUsername()))) {
            return new BasePayload(400, "User with username already exists.");
        }

        Locality locality = localityRepository.find(signupUser.getLocality());
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

        Official official = officialRepository.getCurrentByPersonId(person.getId());
        UserRole role = getRoleByOfficial(official);

        User user = new User(
                person.getId(),
                signupUser.getUsername(),
                encoder.encode(signupUser.getPassword()),
                role
        );

        userRepository.insert(user);
        LoginedUser loginedUser = login(user.getUsername(), signupUser.getPassword());
        UserDTO userDTO = new UserDTO(
                loginedUser.username(),
                role,
                loginedUser.jwtToken(),
                person.getId(),
                official.getId(),
                person.getName() + " " + person.getSurname()
        );

        return new PayloadWithUser(200, userDTO);
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
