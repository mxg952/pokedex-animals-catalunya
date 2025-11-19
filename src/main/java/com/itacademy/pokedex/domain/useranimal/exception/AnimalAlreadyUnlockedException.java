package com.itacademy.pokedex.domain.useranimal.exception;

public class AnimalAlreadyUnlockedException extends RuntimeException {
    public AnimalAlreadyUnlockedException(String message) {
        super(message);
    }
}
