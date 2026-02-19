package com.plazoleta.plazoleta_service.infrastructure.rest.mapper;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.CrearRestauranteRequest;

public class RestauranteRestMapper {
    public static Restaurante toDomain(CrearRestauranteRequest request) {
        return new Restaurante(
                request.getNombre(),
                request.getNit(),
                request.getDireccion(),
                request.getTelefono(),
                request.getUrlLogo(),
                request.getIdPropietario()
        );
    }
}
