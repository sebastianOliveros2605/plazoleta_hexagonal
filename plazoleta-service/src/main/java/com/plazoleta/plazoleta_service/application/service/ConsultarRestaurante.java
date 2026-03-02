package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarRestauranteUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarRestaurante implements IConsultarRestauranteUseCase {

    private final IRestauranteRepositoryPort restauranteRepositoryPort;

    @Override
    public Long obtenerIdRestaurantePorPropietario(Integer idPropietario) {
        Restaurante restaurante = restauranteRepositoryPort
                .buscarPorIdPropietario(idPropietario)
                .orElseThrow(RestauranteNoExisteException::new);
        return restaurante.getId();
    }

    @Override
    public PaginacionResultado<Restaurante> listarRestaurantes(int page, int size) {
        return restauranteRepositoryPort.listar(page, size);
    }
}
