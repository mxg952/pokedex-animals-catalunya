package com.itacademy.pokedex.domain.useranimal.exception;

public class UserAnimalNotFoundException extends RuntimeException {
    public UserAnimalNotFoundException(String message) {
        super("No hem trobat aquest animal...");
    }
}
