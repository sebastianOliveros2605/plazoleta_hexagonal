package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;

public interface IConsultarRestauranteUseCase {

    Long obtenerIdRestaurantePorPropietario(Integer idPropietario);
    PaginacionResultado<Restaurante> listarRestaurantes(int page, int size);
}
