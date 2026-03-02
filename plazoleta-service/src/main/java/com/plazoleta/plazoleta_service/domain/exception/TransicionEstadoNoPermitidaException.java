package com.plazoleta.plazoleta_service.domain.exception;

public class TransicionEstadoNoPermitidaException extends RuntimeException {

    public TransicionEstadoNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
