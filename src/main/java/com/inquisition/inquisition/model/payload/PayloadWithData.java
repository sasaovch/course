package com.inquisition.inquisition.model.payload;

import lombok.Data;
import lombok.Getter;

@Getter
public class PayloadWithData<T> implements Payload {
    private final Integer code;
    private final String message;
    private final T data;

    public PayloadWithData(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public PayloadWithData(Integer code, T data) {
        this.code = code;
        this.message = "";
        this.data = data;
    }

    public T data() {
        return data;
    };

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
