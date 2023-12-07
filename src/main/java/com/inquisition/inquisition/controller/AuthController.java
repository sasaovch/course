package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.jwt.JwtUtils;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.user.LoginUser;
import com.inquisition.inquisition.model.user.SignupUser;
import com.inquisition.inquisition.repository.UserRepository;
import com.inquisition.inquisition.service.AuthenticationService;
import com.inquisition.inquisition.service.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
//    private final AuthenticationManager authenticationManager;
//
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder encoder;
//
//    private final JwtUtils jwtUtils;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder encoder, JwtUtils jwtUtils,
                          AuthenticationServiceImpl authenticationService) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.encoder = encoder;
//        this.jwtUtils = jwtUtils;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Payload> authenticateUser(@Valid @RequestBody LoginUser loginRequest) {

//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<Payload> registerUser(@Valid @RequestBody SignupUser signUpRequest) {
//        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        // Create new user's account
//        User user = new User(
//                signUpRequest.getUsername(),
//                encoder.encode(signUpRequest.getPassword()),
//                signUpRequest.getRole()
//        );
//
//        userRepository.save(user);
        Payload payload = authenticationService.registerUser(signUpRequest);
        if (payload.code() == 400) {
            return ResponseEntity
            .badRequest()
            .body(payload);
        }

        return ResponseEntity.ok(payload);
    }
}
