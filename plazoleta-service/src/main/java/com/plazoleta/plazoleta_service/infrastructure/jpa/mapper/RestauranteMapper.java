package com.plazoleta.plazoleta_service.infrastructure.jpa.mapper;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {

    RestauranteEntity toEntity(Restaurante restaurante);

    Restaurante toDomain(RestauranteEntity restauranteEntity);
}
