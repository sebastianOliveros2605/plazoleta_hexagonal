package com.plazoleta.plazoleta_service.infrastructure.jpa.mapper;

import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.CategoriaEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PlatoEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlatoMapper {

    @Mapping(target = "idRestaurante", source = "restaurante.id")
    @Mapping(target = "idCategoria", source = "categoria.id")
    Plato toDomain(PlatoEntity entity);

    @Mapping(target = "restaurante", source = "idRestaurante", qualifiedByName = "toRestauranteEntity")
    @Mapping(target = "categoria", source = "idCategoria", qualifiedByName = "toCategoriaEntity")
    PlatoEntity toEntity(Plato domain);

    @Named("toRestauranteEntity")
    default RestauranteEntity toRestauranteEntity(Long idRestaurante) {
        if (idRestaurante == null) {
            return null;
        }
        RestauranteEntity restauranteEntity = new RestauranteEntity();
        restauranteEntity.setId(idRestaurante);
        return restauranteEntity;
    }

    @Named("toCategoriaEntity")
    default CategoriaEntity toCategoriaEntity(Long idCategoria) {
        if (idCategoria == null) {
            return null;
        }
        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setId(idCategoria);
        return categoriaEntity;
    }
}
