package com.itacademy.pokedex.exceptions;

public class UserAnimalNotFound extends RuntimeException {
    public UserAnimalNotFound(String message) {
        super(message);
    }
}
