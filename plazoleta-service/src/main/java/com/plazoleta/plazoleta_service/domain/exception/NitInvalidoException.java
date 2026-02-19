package com.plazoleta.plazoleta_service.domain.exception;

public class NitInvalidoException extends RuntimeException {

    public NitInvalidoException() {
        super("El NIT debe contener únicamente números.");
    }
}
