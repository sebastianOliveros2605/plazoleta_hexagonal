package com.plazoleta.trazabilidad_service.infrastructure.rest.dto.request;

import com.plazoleta.trazabilidad_service.domain.model.EstadoPedidoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TrazabilidadRequestDTO {

    @NotNull
    private Long idPedido;
    @NotNull
    private Long idRestaurante;
    private Integer idCliente;
    private String correoCliente;
    private Date fecha;
    private EstadoPedidoEnum estadoAnterior;
    @NotNull
    private EstadoPedidoEnum estadoNuevo;
    private Integer idEmpleado;
    private String correoEmpleado;
}
