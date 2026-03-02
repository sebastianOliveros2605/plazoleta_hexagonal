package com.plazoleta.plazoleta_service.domain.exception;

public class PedidoNoAutorizadoException extends RuntimeException {

    public PedidoNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
