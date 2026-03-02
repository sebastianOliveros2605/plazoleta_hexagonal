package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarPlatoServiceTest {

    @Mock
    private IPlatoRepositoryPort platoRepositoryPort;

    @InjectMocks
    private ConsultarPlatoService consultarPlatoService;

    @Test
    void listarPlatosPorRestaurante_SinCategoria_DebeDelegarYRetornarPagina() {
        PaginacionResultado<Plato> esperado = new PaginacionResultado<>(List.of(new Plato()), 0, 10, 1, 1, true);
        when(platoRepositoryPort.listarPorRestaurante(1L, null, 0, 10)).thenReturn(esperado);

        PaginacionResultado<Plato> resultado = consultarPlatoService.listarPlatosPorRestaurante(1L, null, 0, 10);

        assertEquals(esperado, resultado);
        verify(platoRepositoryPort).listarPorRestaurante(1L, null, 0, 10);
    }

    @Test
    void listarPlatosPorRestaurante_ConCategoria_DebeDelegarYRetornarPagina() {
        PaginacionResultado<Plato> esperado = new PaginacionResultado<>(List.of(), 0, 5, 0, 0, true);
        when(platoRepositoryPort.listarPorRestaurante(1L, 2L, 0, 5)).thenReturn(esperado);

        PaginacionResultado<Plato> resultado = consultarPlatoService.listarPlatosPorRestaurante(1L, 2L, 0, 5);

        assertEquals(esperado, resultado);
        verify(platoRepositoryPort).listarPorRestaurante(1L, 2L, 0, 5);
    }
}
