package com.plazoleta.plazoleta_service.infrastructure.rest.mapper;

import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.ModificarPlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PlatoResponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlatoRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Plato toDomain(PlatoRequestDTO dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "precio", source = "precio")
    Plato toDomain(ModificarPlatoRequestDTO dto);

    PlatoResponseDTO toResponse(Plato domain);

    List<PlatoResponseDTO> toResponseList(List<Plato> platos);
}
