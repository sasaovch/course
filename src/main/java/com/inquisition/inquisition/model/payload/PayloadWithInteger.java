package com.inquisition.inquisition.model.payload;

public class PayloadWithInteger extends PayloadWithData<Integer>{
    public PayloadWithInteger(Integer code, String message, Integer data) {
        super(code, message, data);
    }
}
