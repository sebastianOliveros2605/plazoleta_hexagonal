package com.plazoleta.plazoleta_service.application.service;

import org.springframework.stereotype.Service;

import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.exception.PrecioInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrearPlatoService implements ICrearPlatoUseCase{

    private final IPlatoRepositoryPort platoRepositoryPort;
    private final IRestauranteRepositoryPort restauranteRepositoryPort;

    @Override
    public void crearPlato(Plato plato, Integer idPropietario) {

        Restaurante restaurante = restauranteRepositoryPort
                .buscarPorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (!restaurante.getIdPropietario().equals(idPropietario)) {
            throw new NoEsPropietarioException();
        }

        if (plato.getPrecio() <= 0) {
            throw new PrecioInvalidoException();
        }
        System.out.println("NOMBRE PLATO A GUARDAR: "+plato.getNombre());
        platoRepositoryPort.guardar(plato);
    }
    
}
