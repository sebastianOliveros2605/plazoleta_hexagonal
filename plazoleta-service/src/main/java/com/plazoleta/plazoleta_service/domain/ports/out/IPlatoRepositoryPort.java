package com.plazoleta.plazoleta_service.domain.ports.out;

import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import java.util.Optional;

import com.plazoleta.plazoleta_service.domain.model.Plato;

public interface IPlatoRepositoryPort {

    Plato guardar(Plato plato);
    Optional<Plato> buscarPorId(Long id);
    PaginacionResultado<Plato> listarPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size);
}
