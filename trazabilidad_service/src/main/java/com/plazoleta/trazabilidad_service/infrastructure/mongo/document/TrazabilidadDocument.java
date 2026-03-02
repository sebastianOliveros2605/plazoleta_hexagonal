package com.plazoleta.trazabilidad_service.infrastructure.mongo.document;

import com.plazoleta.trazabilidad_service.domain.model.EstadoPedidoEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "trazabilidad_pedido")
public class TrazabilidadDocument {

    @Id
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
