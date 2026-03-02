package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Plato;

public interface IConsultarPlatoUseCase {

    PaginacionResultado<Plato> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size);
}
