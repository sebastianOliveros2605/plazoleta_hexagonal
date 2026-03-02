package com.plazoleta.trazabilidad_service.infrastructure.rest.mapper;

import com.plazoleta.trazabilidad_service.domain.model.Trazabilidad;
import com.plazoleta.trazabilidad_service.infrastructure.rest.dto.request.TrazabilidadRequestDTO;
import com.plazoleta.trazabilidad_service.infrastructure.rest.dto.response.TrazabilidadResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrazabilidadRestMapper {

    @Mapping(target = "id", ignore = true)
    Trazabilidad toModel(TrazabilidadRequestDTO request);

    TrazabilidadResponseDTO toResponse(Trazabilidad model);

    List<TrazabilidadResponseDTO> toResponseList(List<Trazabilidad> models);
}
