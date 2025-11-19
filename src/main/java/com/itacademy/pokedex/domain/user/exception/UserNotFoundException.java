package com.itacademy.pokedex.domain.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String name) {
        super("No existeix cap usuari amb aquest nom..." + name);
    }
}
