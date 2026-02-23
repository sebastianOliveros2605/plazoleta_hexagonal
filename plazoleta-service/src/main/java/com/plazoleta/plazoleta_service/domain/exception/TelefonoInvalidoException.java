package com.plazoleta.plazoleta_service.domain.exception;

public class TelefonoInvalidoException extends RuntimeException {

    public TelefonoInvalidoException() {
        super("El telefono debe contener maximo 13 caracteres, solo numeros y puede iniciar con +.");
    }
}
