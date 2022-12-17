package ru.pasha.messenger.utils.validator;

public interface Validator<T> {

    boolean validate(T object);
}
