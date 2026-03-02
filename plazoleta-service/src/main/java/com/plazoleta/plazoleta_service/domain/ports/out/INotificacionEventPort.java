package com.plazoleta.plazoleta_service.domain.ports.out;

public interface INotificacionEventPort {

    void notificarPedidoListo(Long idPedido, Integer idCliente, String celularDestino, String mensaje, String pin);
}
