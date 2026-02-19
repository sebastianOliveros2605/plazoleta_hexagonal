package com.plazoleta.plazoleta_service.infrastructure.jpa.mapper;

import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PlatoEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;

public class PlatoMapper {

    public static Plato toDomain(PlatoEntity entity) {
        if (entity == null)
            return null;
        Plato plato = new Plato();
        plato.setId(entity.getId());
        plato.setNombre(entity.getNombre());
        plato.setDescripcion(entity.getDescripcion());
        plato.setPrecio(entity.getPrecio());
        plato.setUrlImagen(entity.getUrlImagen());
        plato.setCategoria(entity.getCategoria());
        plato.setIdRestaurante(entity.getRestaurante().getId());
        plato.setActivo(entity.getActivo());

        return plato;
    }

    public static PlatoEntity toEntity(Plato domain) {
        if (domain == null)
            return null;
        PlatoEntity platoEntity = new PlatoEntity();
        platoEntity.setId(domain.getId());
        platoEntity.setNombre(domain.getNombre());
        platoEntity.setDescripcion(domain.getDescripcion());
        platoEntity.setPrecio(domain.getPrecio());
        RestauranteEntity restaurante = new RestauranteEntity();
        restaurante.setId(domain.getIdRestaurante());
        platoEntity.setRestaurante(restaurante);
        platoEntity.setCategoria(domain.getCategoria());
        platoEntity.setActivo(domain.getActivo());
        return platoEntity;
    }
}
