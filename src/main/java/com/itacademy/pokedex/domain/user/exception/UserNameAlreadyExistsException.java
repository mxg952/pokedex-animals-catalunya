package com.itacademy.pokedex.domain.user.exception;

public class UserNameAlreadyExistsException extends RuntimeException {
    public UserNameAlreadyExistsException(String name) {
        super("Username already exists: " + name);
    }
}
