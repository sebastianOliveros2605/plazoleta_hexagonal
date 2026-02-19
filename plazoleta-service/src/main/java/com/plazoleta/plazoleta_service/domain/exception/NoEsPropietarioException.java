package com.plazoleta.plazoleta_service.domain.exception;

public class NoEsPropietarioException extends RuntimeException {

    public NoEsPropietarioException() {
        super("No es propietario del restaurante.");
    }
}
