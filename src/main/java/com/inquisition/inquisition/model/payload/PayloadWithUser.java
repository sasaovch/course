package com.inquisition.inquisition.model.payload;

import com.inquisition.inquisition.model.user.UserDTO;
import com.inquisition.inquisition.model.user.UserRole;

public class PayloadWithUser extends PayloadWithData<UserDTO>{
    public PayloadWithUser(Integer code, String message, UserDTO data) {
        super(code, message, data);
    }
}
