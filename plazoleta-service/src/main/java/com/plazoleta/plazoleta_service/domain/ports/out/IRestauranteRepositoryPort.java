package com.plazoleta.plazoleta_service.domain.ports.out;

import java.util.Optional;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;

public interface IRestauranteRepositoryPort {
    Restaurante guardar(Restaurante restaurante);
    Optional<Restaurante> buscarPorId(Long idRestaurante);
}
