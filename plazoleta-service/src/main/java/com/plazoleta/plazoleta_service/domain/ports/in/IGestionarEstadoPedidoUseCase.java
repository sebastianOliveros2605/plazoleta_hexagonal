package com.plazoleta.plazoleta_service.domain.ports.in;

public interface IGestionarEstadoPedidoUseCase {

    void asignarPedidoEnPreparacion(Long idPedido, Integer idEmpleado, String correoEmpleado);

    void marcarPedidoListo(Long idPedido, Integer idEmpleado, String correoEmpleado);

    void marcarPedidoEntregado(Long idPedido, Integer idEmpleado, String correoEmpleado, String pinSeguridad);

    void cancelarPedido(Long idPedido, Integer idCliente, String correoCliente);
}
