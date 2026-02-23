package com.plazoleta.plazoleta_service.infrastructure.rest.mapper;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.CrearRestauranteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestauranteRestMapper {

    @Mapping(target = "id", ignore = true)
    Restaurante toDomain(CrearRestauranteRequest request);
}
