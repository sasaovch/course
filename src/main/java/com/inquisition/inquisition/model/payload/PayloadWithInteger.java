package com.inquisition.inquisition.model.payload;

import lombok.Getter;

@Getter
public class PayloadWithInteger extends PayloadWithData<Integer>{
    public PayloadWithInteger(Integer code, String message, Integer data) {
        super(code, message, data);
    }
}
