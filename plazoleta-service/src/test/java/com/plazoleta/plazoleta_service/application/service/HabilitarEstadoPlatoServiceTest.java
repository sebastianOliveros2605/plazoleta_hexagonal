package com.plazoleta.plazoleta_service.application.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.exception.PlatoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;

@ExtendWith(MockitoExtension.class)
class HabilitarEstadoPlatoServiceTest {

    @Mock
    private IPlatoRepositoryPort platoRepositoryPort;

    @Mock
    private IRestauranteRepositoryPort restauranteRepositoryPort;

    @InjectMocks
    private HabilitarEstadoPlatoService habilitarEstadoPlatoService;

    @Test
    void habilitarDeshabilitarPlato_CuandoDatosValidos_DeberiaActualizarEstado() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setIdRestaurante(20L);
        plato.setActivo(true);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(20L);
        restaurante.setIdPropietario(9);

        when(platoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(plato));
        when(restauranteRepositoryPort.buscarPorId(20L)).thenReturn(Optional.of(restaurante));

        habilitarEstadoPlatoService.habilitarDeshabilitarPlato(1L, 9, false);

        assertFalse(plato.getActivo());
        verify(platoRepositoryPort).guardar(plato);
    }

    @Test
    void habilitarDeshabilitarPlato_CuandoHabilita_DeberiaActivarPlato() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setIdRestaurante(20L);
        plato.setActivo(false);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(20L);
        restaurante.setIdPropietario(9);

        when(platoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(plato));
        when(restauranteRepositoryPort.buscarPorId(20L)).thenReturn(Optional.of(restaurante));

        habilitarEstadoPlatoService.habilitarDeshabilitarPlato(1L, 9, true);

        assertTrue(plato.getActivo());
        verify(platoRepositoryPort).guardar(plato);
    }

    @Test
    void habilitarDeshabilitarPlato_CuandoPlatoNoExiste_DebeLanzar() {
        when(platoRepositoryPort.buscarPorId(50L)).thenReturn(Optional.empty());

        assertThrows(
                PlatoNoExisteException.class,
                () -> habilitarEstadoPlatoService.habilitarDeshabilitarPlato(50L, 1, true));
    }

    @Test
    void habilitarDeshabilitarPlato_CuandoRestauranteNoExiste_DebeLanzar() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setIdRestaurante(20L);

        when(platoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(plato));
        when(restauranteRepositoryPort.buscarPorId(20L)).thenReturn(Optional.empty());

        assertThrows(
                RestauranteNoExisteException.class,
                () -> habilitarEstadoPlatoService.habilitarDeshabilitarPlato(1L, 1, true));
    }

    @Test
    void habilitarDeshabilitarPlato_CuandoNoEsPropietario_DebeLanzar() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setIdRestaurante(20L);
        plato.setActivo(true);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(20L);
        restaurante.setIdPropietario(99);

        when(platoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(plato));
        when(restauranteRepositoryPort.buscarPorId(20L)).thenReturn(Optional.of(restaurante));

        assertThrows(
                NoEsPropietarioException.class,
                () -> habilitarEstadoPlatoService.habilitarDeshabilitarPlato(1L, 9, false));
    }
}
