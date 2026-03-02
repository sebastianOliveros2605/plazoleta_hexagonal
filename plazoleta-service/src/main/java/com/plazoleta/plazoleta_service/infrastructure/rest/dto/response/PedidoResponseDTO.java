package com.plazoleta.plazoleta_service.infrastructure.rest.dto.response;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PedidoResponseDTO {
    private Long id;
    private Integer idCliente;
    private Long idRestaurante;
    private EstadoPedidoEnum estado;
    private Date fechaCreacion;
    private Integer idEmpleado;
    private List<DetallePedidoResponseDTO> detallePedido;
    private Date fechaEntrega;
}
