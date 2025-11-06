package com.itacademy.pokedex.domain.user.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String name) {
        super("Username already exists: " + name);
    }
}
