package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;

public interface ICrearRestauranteUseCase {
    void ejecutar(Restaurante restaurante);
}
