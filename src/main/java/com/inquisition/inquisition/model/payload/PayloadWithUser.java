package com.inquisition.inquisition.model.payload;

import com.inquisition.inquisition.model.user.UserDTO;
import com.inquisition.inquisition.model.user.UserRole;
import lombok.Data;
import lombok.Getter;

@Getter
public class PayloadWithUser extends PayloadWithData<UserDTO>{
    public PayloadWithUser(Integer code, String message, UserDTO data) {
        super(code, message, data);
    }
}
