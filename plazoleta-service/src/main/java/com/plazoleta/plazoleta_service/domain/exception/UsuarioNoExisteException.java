package com.plazoleta.plazoleta_service.domain.exception;

public class UsuarioNoExisteException extends RuntimeException {

    public UsuarioNoExisteException() {
        super("El usuario no existe.");
    }
}
