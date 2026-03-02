package com.plazoleta.plazoleta_service.infrastructure.rest.mapper;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.CrearRestauranteRequest;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.RestauranteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestauranteRestMapper {

    @Mapping(target = "id", ignore = true)
    Restaurante toDomain(CrearRestauranteRequest request);

    RestauranteResponseDTO toResponseDTO(Restaurante restaurante);

    List<RestauranteResponseDTO> toResponseList(List<Restaurante> restaurantes);
}
