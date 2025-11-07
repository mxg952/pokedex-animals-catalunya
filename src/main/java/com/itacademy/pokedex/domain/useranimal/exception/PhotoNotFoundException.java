package com.itacademy.pokedex.domain.useranimal.exception;

public class PhotoNotFoundException extends RuntimeException {
    public PhotoNotFoundException(Long photoId) {
        super("La foto amb id:" + photoId + "no s'ha torbat");
    }
}
