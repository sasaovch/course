package com.inquisition.inquisition;

import lombok.Data;

@Data
public class Pair<T, S> {
    public Pair() {}
    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }
    private T first;
    private S second;

    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair<>(first, second);
    }
}
