package com.plazoleta.plazoleta_service.domain.exception;

public class PedidoInvalidoException extends RuntimeException {

    public PedidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
