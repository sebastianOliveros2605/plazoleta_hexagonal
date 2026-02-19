package com.plazoleta.plazoleta_service.domain.ports.out;

import com.plazoleta.plazoleta_service.domain.model.Plato;

public interface IModificarPlatoUseCase {
    void modificarPlato(Plato plato, Integer idPropietario);
}
