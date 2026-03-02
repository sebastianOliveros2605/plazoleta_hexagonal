package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.PlatoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.ICambiarEstadoActivoPlato;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabilitarEstadoPlatoService implements ICambiarEstadoActivoPlato {
    private final IPlatoRepositoryPort platoRepositoryPort;
    private final IRestauranteRepositoryPort restauranteRepositoryPort;

    @Override
    public void habilitarDeshabilitarPlato(Long idPlato, Integer idPropietario, Boolean habilitar) {

        Plato plato = platoRepositoryPort
                .buscarPorId(idPlato)
                .orElseThrow(PlatoNoExisteException::new);

        Restaurante restaurante = restauranteRepositoryPort
                .buscarPorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (!restaurante.getIdPropietario().equals(idPropietario)) {
            throw new NoEsPropietarioException();
        }

        plato.habilitarDeshabilitarPlato(habilitar);
        platoRepositoryPort.guardar(plato);
    }
}
