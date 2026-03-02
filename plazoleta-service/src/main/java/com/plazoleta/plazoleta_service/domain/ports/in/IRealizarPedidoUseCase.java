package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.Pedido;

public interface IRealizarPedidoUseCase {
    void realizarPedido(Pedido pedido);
}
