package com.itacademy.pokedex.domain.useranimal.exception;

public class UnauthorizedPhotoAccessException extends RuntimeException {
    public UnauthorizedPhotoAccessException(Long photoId) {
        super("No tens accés a aquesta photo...");
    }
}
