package ru.npv.exam.jc.app.domain.app.utils;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
