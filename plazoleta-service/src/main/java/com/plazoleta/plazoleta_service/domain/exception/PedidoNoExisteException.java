package com.plazoleta.plazoleta_service.domain.exception;

public class PedidoNoExisteException extends RuntimeException {

    public PedidoNoExisteException(Long idPedido) {
        super("No existe un pedido con id " + idPedido + ".");
    }
}
