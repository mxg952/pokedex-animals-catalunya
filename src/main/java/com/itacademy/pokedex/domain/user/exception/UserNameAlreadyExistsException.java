package com.itacademy.pokedex.domain.user.exception;

public class UserNameAlreadyExistsException extends RuntimeException {
    public UserNameAlreadyExistsException(String name) {
        super("Aquest nom d'usuari ja existeix: " + name + ". Prova amb un altre nom.");
    }
}
