package com.plazoleta.plazoleta_service.domain.exception;

public class TelefonoInvalidoException extends RuntimeException {

    public TelefonoInvalidoException() {
        super("El teléfono debe tener máximo 13 caracteres.");
    }
}
