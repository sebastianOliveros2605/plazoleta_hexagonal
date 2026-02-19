package com.plazoleta.plazoleta_service.infrastructure.jpa.mapper;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;

public class RestauranteMapper {

    public static RestauranteEntity toEntity(Restaurante restaurante){
        RestauranteEntity restauranteEntity = new RestauranteEntity();
        restauranteEntity.setNombre(restaurante.getNombre());
        restauranteEntity.setNit(restaurante.getNit());
        restauranteEntity.setDireccion(restaurante.getDireccion());
        restauranteEntity.setTelefono(restaurante.getTelefono());
        restauranteEntity.setUrlLogo(restaurante.getUrlLogo());
        restauranteEntity.setIdPropietario(restaurante.getIdPropietario());
        return restauranteEntity;
    }

    public static Restaurante toDomain(RestauranteEntity restauranteEntity){
        Restaurante restaurante = new Restaurante(
            restauranteEntity.getNombre(),
            restauranteEntity.getNit(),
            restauranteEntity.getDireccion(),
            restauranteEntity.getTelefono(),
            restauranteEntity.getUrlLogo(),
            restauranteEntity.getIdPropietario()
        );
        return restaurante;
    }

}
