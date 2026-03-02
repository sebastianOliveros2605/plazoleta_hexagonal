package com.plazoleta.notificaciones_service.infrastructure.rabbitmq.mapper;

import com.plazoleta.notificaciones_service.domain.model.Notificacion;
import com.plazoleta.notificaciones_service.infrastructure.rabbitmq.dto.NotificacionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificacionEventMapper {

    Notificacion toModel(NotificacionDTO dto);

    NotificacionDTO toDto(Notificacion model);
}
