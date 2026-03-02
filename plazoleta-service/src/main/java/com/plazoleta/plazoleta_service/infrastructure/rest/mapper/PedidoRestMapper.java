package com.plazoleta.plazoleta_service.infrastructure.rest.mapper;

import com.plazoleta.plazoleta_service.domain.model.DetallePedido;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.DetallePedidoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PedidoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.DetallePedidoResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoRestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaEntrega", ignore = true)
    @Mapping(target = "idEmpleado", ignore = true)
    @Mapping(target = "pinSeguridad", ignore = true)
    @Mapping(target = "detallePedido", source = "detallePedido")
    Pedido toDomain(PedidoRequestDTO request);

    @Mapping(target = "idPedido", ignore = true)
    DetallePedido toDetailDomain(DetallePedidoRequestDTO detailRequest);

    PedidoResponseDTO toResponse(Pedido pedido);

    DetallePedidoResponseDTO toResponse(DetallePedido detallePedido);

    List<PedidoResponseDTO> toResponseList(List<Pedido> pedidos);
}
