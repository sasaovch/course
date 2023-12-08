package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.user.LoginUser;
import com.inquisition.inquisition.model.user.SignupUser;
import com.inquisition.inquisition.service.intr.AuthenticationService;
import com.inquisition.inquisition.service.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value="/api/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/signin",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payload> authenticateUser(@Valid @RequestBody LoginUser loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Payload> registerUser(@Valid @RequestBody SignupUser signUpRequest) {
        Payload payload = authenticationService.registerUser(signUpRequest);
        if (payload.code() == 400) {
            return ResponseEntity
            .badRequest()
            .body(payload);
        }

        return ResponseEntity.ok(payload);
    }
}
