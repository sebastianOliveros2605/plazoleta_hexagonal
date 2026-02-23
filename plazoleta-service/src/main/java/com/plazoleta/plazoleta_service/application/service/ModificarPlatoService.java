package com.plazoleta.plazoleta_service.application.service;

import org.springframework.stereotype.Service;

import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.exception.PlatoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.IModificarPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModificarPlatoService implements IModificarPlatoUseCase {

    private final IPlatoRepositoryPort platoRepositoryPort;
    private final IRestauranteRepositoryPort restauranteRepositoryPort;

    @Override
    public void modificarPlato(Plato platoModificado, Integer idPropietario) {

        Plato plato = platoRepositoryPort
                .buscarPorId(platoModificado.getId())
                .orElseThrow(PlatoNoExisteException::new);

        Restaurante restaurante = restauranteRepositoryPort
                .buscarPorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (!restaurante.getIdPropietario().equals(idPropietario)) {
            throw new NoEsPropietarioException();
        }

        plato.actualizarPrecioYDescripcion(platoModificado.getDescripcion(), platoModificado.getPrecio());
        platoRepositoryPort.guardar(plato);
    }
}
