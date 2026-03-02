package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Pedido;

public interface IConsultarPedidoUseCase {

    PaginacionResultado<Pedido> listarPedidosPorEstado(Integer idEmpleado, EstadoPedidoEnum estado, int page, int size);
}
