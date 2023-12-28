package com.inquisition.inquisition.model.payload;

import com.inquisition.inquisition.model.user.payload.UserPayload;
import lombok.Getter;

@Getter
public class PayloadWithUser extends PayloadWithData<UserPayload>{
    public PayloadWithUser(Integer code, UserPayload data) {
        super(code, data);
    }
}
