package com.plazoleta.plazoleta_service.domain.ports.out;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;

import java.util.Optional;

public interface IRestauranteRepositoryPort {
    Restaurante guardar(Restaurante restaurante);
    Optional<Restaurante> buscarPorId(Long idRestaurante);
    Optional<Restaurante> buscarPorIdPropietario(Integer idPropietario);
    PaginacionResultado<Restaurante> listar(int page, int size);
}
