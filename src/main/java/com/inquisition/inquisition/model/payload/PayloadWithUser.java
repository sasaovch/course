package com.inquisition.inquisition.model.payload;

import com.inquisition.inquisition.model.user.UserDTO;
import lombok.Getter;

@Getter
public class PayloadWithUser extends PayloadWithData<UserDTO>{
    public PayloadWithUser(Integer code, UserDTO data) {
        super(code, data);
    }
}
