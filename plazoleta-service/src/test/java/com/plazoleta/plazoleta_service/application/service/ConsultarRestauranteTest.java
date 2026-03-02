package com.plazoleta.plazoleta_service.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;

@ExtendWith(MockitoExtension.class)
class ConsultarRestauranteTest {

    @Mock
    private IRestauranteRepositoryPort restauranteRepositoryPort;

    @InjectMocks
    private ConsultarRestaurante consultarRestaurante;

    @Test
    void listarRestaurantes_CuandoSeSolicitaPaginado_DebeRetornarPagina() {
        Restaurante r1 = new Restaurante();
        r1.setNombre("Arepas House");

        Restaurante r2 = new Restaurante();
        r2.setNombre("Burger Spot");

        PaginacionResultado<Restaurante> page = new PaginacionResultado<>(List.of(r1, r2), 0, 2, 5, 3, false);
        when(restauranteRepositoryPort.listar(0, 2)).thenReturn(page);

        PaginacionResultado<Restaurante> result = consultarRestaurante.listarRestaurantes(0, 2);

        assertEquals(2, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        verify(restauranteRepositoryPort).listar(0, 2);
    }
}
