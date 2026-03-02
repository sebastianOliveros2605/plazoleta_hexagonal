package com.plazoleta.plazoleta_service.domain.exception;

public class UsuarioConPedidosPendientes extends RuntimeException {

    public UsuarioConPedidosPendientes() {
        super("El usuario tiene pedidos sin finalizar.");
    }
}
