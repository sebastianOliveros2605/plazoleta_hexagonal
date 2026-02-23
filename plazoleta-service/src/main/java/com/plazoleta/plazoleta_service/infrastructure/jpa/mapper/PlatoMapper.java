package com.plazoleta.plazoleta_service.infrastructure.jpa.mapper;

import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PlatoEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatoMapper {

    @Mapping(target = "idRestaurante", source = "restaurante.id")
    Plato toDomain(PlatoEntity entity);

    @Mapping(target = "restaurante", source = "idRestaurante")
    PlatoEntity toEntity(Plato domain);

    default RestauranteEntity map(Long idRestaurante) {
        if (idRestaurante == null) {
            return null;
        }
        RestauranteEntity restauranteEntity = new RestauranteEntity();
        restauranteEntity.setId(idRestaurante);
        return restauranteEntity;
    }
}
