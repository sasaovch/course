package com.inquisition.inquisition.model.payload;

import com.inquisition.inquisition.model.user.LoginedUser;
import com.inquisition.inquisition.model.user.UserDTO;
import lombok.Getter;

@Getter
public class PayloadWithUser extends PayloadWithData<UserDTO>{
    public PayloadWithUser(Integer code, String message, UserDTO data) {
        super(code, message, data);
    }
    public PayloadWithUser(Integer code, UserDTO data) {
        super(code, data);
    }
}
