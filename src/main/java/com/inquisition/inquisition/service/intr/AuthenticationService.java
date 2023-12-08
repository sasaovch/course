package com.inquisition.inquisition.service.intr;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.user.LoginUser;
import com.inquisition.inquisition.model.user.SignupUser;

public interface AuthenticationService {
    Payload registerUser(SignupUser user);

    Payload authenticateUser(LoginUser user);
}
