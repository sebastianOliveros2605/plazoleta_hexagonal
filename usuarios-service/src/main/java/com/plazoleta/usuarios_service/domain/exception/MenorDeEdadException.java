package com.plazoleta.usuarios_service.domain.exception;

public class MenorDeEdadException extends RuntimeException {
    public MenorDeEdadException() {
        super("El usuario debe ser mayor de edad");
    }
}

