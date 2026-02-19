package com.plazoleta.plazoleta_service.application.service;

import org.springframework.stereotype.Service;

import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.exception.PlatoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.PrecioInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.out.IModificarPlatoUseCase;
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

        if (platoModificado.getPrecio() <= 0) {
            throw new PrecioInvalidoException();
        }

        plato.setNombre(platoModificado.getNombre());
        plato.setDescripcion(platoModificado.getDescripcion());
        plato.setPrecio(platoModificado.getPrecio());
        plato.setCategoria(platoModificado.getCategoria());
        System.out.println("ID DEL PLATO A MODIFICAR: "+plato.getId());
        platoRepositoryPort.guardar(plato);
    }
}