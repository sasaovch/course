package com.inquisition.inquisition.service;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithData;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.LoginUser;
import com.inquisition.inquisition.model.user.SignupUser;
import com.inquisition.inquisition.model.user.UserDTO;

public interface AuthenticationService {
    Payload registerUser(SignupUser user);
    Payload authenticateUser(LoginUser user);
}
