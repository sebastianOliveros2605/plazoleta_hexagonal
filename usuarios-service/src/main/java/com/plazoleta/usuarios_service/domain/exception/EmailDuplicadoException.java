package com.plazoleta.usuarios_service.domain.exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException() {
        super("El correo ya esta registrado");
    }
}

