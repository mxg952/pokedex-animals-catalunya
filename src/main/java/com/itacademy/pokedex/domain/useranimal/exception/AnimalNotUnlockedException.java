package com.itacademy.pokedex.domain.useranimal.exception;

public class AnimalNotUnlockedException extends RuntimeException {
    public AnimalNotUnlockedException(String message) {
        super("Aquest animal encara està bloquejat...");
    }
}
