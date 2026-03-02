package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarPlatoService implements IConsultarPlatoUseCase {

    private final IPlatoRepositoryPort platoRepositoryPort;

    @Override
    public PaginacionResultado<Plato> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size) {
        return platoRepositoryPort.listarPorRestaurante(idRestaurante, idCategoria, page, size);
    }
}
