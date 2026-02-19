package com.plazoleta.plazoleta_service.domain.model;

public enum EstadoPedidoEnum {
    PENDIENTE("Pedido pendiente."),
    EN_PREPARACION("Pedido en preparacion."),
    LISTO("Pedido listo para entrega."),
    ENTREGADO("Pedido entregado."),
    CANCELADO("Pedido cancelado");

    private final String descripcion;

    EstadoPedidoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
