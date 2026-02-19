package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.Plato;

public interface ICrearPlatoUseCase {
    void crearPlato(Plato plato, Integer idPropietario);
}
