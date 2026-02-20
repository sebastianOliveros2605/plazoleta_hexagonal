package com.plazoleta.plazoleta_service.application.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.plazoleta.plazoleta_service.domain.exception.*;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.out.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CrearPlatoServiceTest {

    @Mock
    private IPlatoRepositoryPort platoRepositoryPort;

    @Mock
    private IRestauranteRepositoryPort restauranteRepositoryPort;

    @InjectMocks
    private CrearPlatoService crearPlatoService;

    @Test
    void crearPlato_CuandoDatosValidos_DeberiaGuardar() {
        // Arrange
        Plato plato = new Plato();
        plato.setIdRestaurante(1L);
        plato.setPrecio(100);

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(5);

        when(restauranteRepositoryPort.buscarPorId(1L))
            .thenReturn(Optional.of(restaurante));

        // Act
        crearPlatoService.crearPlato(plato, 5);

        // Assert
        verify(platoRepositoryPort).guardar(plato);
    }

    @Test
    void crearPlato_SiRestauranteNoExiste_DebeLanzarExcepcion() {
        when(restauranteRepositoryPort.buscarPorId(1L))
            .thenReturn(Optional.empty());

        Plato plato = new Plato();
        plato.setIdRestaurante(1L);

        assertThrows(RestauranteNoExisteException.class,
            () -> crearPlatoService.crearPlato(plato, 5));
    }

    @Test
    void crearPlato_SiNoEsPropietario_DebeLanzarExcepcion() {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(9);

        when(restauranteRepositoryPort.buscarPorId(1L))
            .thenReturn(Optional.of(restaurante));

        Plato plato = new Plato();
        plato.setPrecio(100);
        plato.setIdRestaurante(1L);

        assertThrows(NoEsPropietarioException.class,
            () -> crearPlatoService.crearPlato(plato, 5));
    }

    @Test
    void crearPlato_SiPrecioInvalido_DebeLanzarExcepcion() {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(5);

        when(restauranteRepositoryPort.buscarPorId(1L))
            .thenReturn(Optional.of(restaurante));

        Plato plato = new Plato();
        plato.setIdRestaurante(1L);
        plato.setPrecio(0);

        assertThrows(PrecioInvalidoException.class,
            () -> crearPlatoService.crearPlato(plato, 5));
    }
}

