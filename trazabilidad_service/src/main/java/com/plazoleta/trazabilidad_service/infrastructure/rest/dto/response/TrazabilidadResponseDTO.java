package com.plazoleta.trazabilidad_service.infrastructure.rest.dto.response;

import com.plazoleta.trazabilidad_service.domain.model.EstadoPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrazabilidadResponseDTO {

    private String id;
    private Long idPedido;
    private Long idRestaurante;
    private Integer idCliente;
    private String correoCliente;
    private Date fecha;
    private EstadoPedidoEnum estadoAnterior;
    private EstadoPedidoEnum estadoNuevo;
    private Integer idEmpleado;
    private String correoEmpleado;
}
