package com.plazoleta.plazoleta_service.infrastructure.rabbitmq.mapper;

import com.plazoleta.plazoleta_service.infrastructure.rabbitmq.dto.NotificacionEventDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificacionEventRabbitMapper {

    NotificacionEventDTO toDto(Long idPedido, Integer idCliente, String celularDestino, String mensaje, String pin);
}
