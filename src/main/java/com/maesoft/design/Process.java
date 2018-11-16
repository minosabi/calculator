package com.maesoft.design;

@FunctionalInterface
public interface Process<S, T> {

    T apply(S s);

}
