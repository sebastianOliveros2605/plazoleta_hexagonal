package com.plazoleta.plazoleta_service.domain.ports.out;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Pedido;

import java.util.List;

public interface IPedidoRepositoryPort {
    Boolean clienteConPedidosEnProceso(Integer idCliente);
    Pedido consultarPedidoPorId(Long idPedido);
    PaginacionResultado<Pedido> listarPorRestauranteYEstado(Long idRestaurante, EstadoPedidoEnum estado, int page, int size);
    Pedido guardar(Pedido pedido);
    List<Long> consultarIdsFinalizadosPorRestaurante(Long idRestaurante);
}
