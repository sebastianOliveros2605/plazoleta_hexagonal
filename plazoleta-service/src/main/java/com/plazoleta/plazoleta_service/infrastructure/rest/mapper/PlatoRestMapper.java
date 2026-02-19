package com.plazoleta.plazoleta_service.infrastructure.rest.mapper;

import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PlatoResponseDTO;

public class PlatoRestMapper {

    public static Plato toDomain(PlatoRequestDTO dto) {
        if (dto == null) return null;

        Plato plato = new Plato();
        plato.setNombre(dto.getNombre());
        plato.setDescripcion(dto.getDescripcion());
        plato.setPrecio(dto.getPrecio());
        plato.setIdRestaurante(dto.getIdRestaurante());
        plato.setCategoria(dto.getCategoria());
        plato.setActivo(true);

        return plato;
    }

    public static PlatoResponseDTO toResponse(Plato domain) {
        if (domain == null) return null;

        PlatoResponseDTO response = new PlatoResponseDTO();
        response.setId(domain.getId());
        response.setNombre(domain.getNombre());
        response.setDescripcion(domain.getDescripcion());
        response.setPrecio(domain.getPrecio());
        response.setActivo(domain.getActivo());

        return response;
    }
}
