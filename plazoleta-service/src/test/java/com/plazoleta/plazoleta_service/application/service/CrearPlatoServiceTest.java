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

    @Mock
    private ICategoriaRepositoryPort categoriaRepositoryPort;

    @InjectMocks
    private CrearPlatoService crearPlatoService;

    @Test
    void crearPlato_CuandoDatosValidos_DeberiaGuardar() {
        // Arrange
        Plato plato = new Plato();
        plato.setNombre("Bandeja Paisa");
        plato.setDescripcion("Plato tradicional");
        plato.setIdRestaurante(1L);
        plato.setIdCategoria(1L);
        plato.setPrecio(100);
        plato.setUrlImagen("https://img.test/plato.png");

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(5);

        when(restauranteRepositoryPort.buscarPorId(1L))
            .thenReturn(Optional.of(restaurante));
        when(categoriaRepositoryPort.buscarPorId(1L))
                .thenReturn(new com.plazoleta.plazoleta_service.domain.model.Categoria());

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
        plato.setNombre("Bandeja Paisa");
        plato.setDescripcion("Plato tradicional");
        plato.setIdRestaurante(1L);
        plato.setIdCategoria(1L);
        plato.setPrecio(100);
        plato.setUrlImagen("https://img.test/plato.png");

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
        plato.setNombre("Bandeja Paisa");
        plato.setDescripcion("Plato tradicional");
        plato.setPrecio(100);
        plato.setIdRestaurante(1L);
        plato.setIdCategoria(1L);
        plato.setUrlImagen("https://img.test/plato.png");

        assertThrows(NoEsPropietarioException.class,
            () -> crearPlatoService.crearPlato(plato, 5));
    }

    @Test
    void crearPlato_SiPrecioInvalido_DebeLanzarExcepcion() {
        Plato plato = new Plato();
        plato.setNombre("Bandeja Paisa");
        plato.setDescripcion("Plato tradicional");
        plato.setIdRestaurante(1L);
        plato.setIdCategoria(1L);
        plato.setPrecio(0);
        plato.setUrlImagen("https://img.test/plato.png");

        assertThrows(PrecioInvalidoException.class,
            () -> crearPlatoService.crearPlato(plato, 5));
    }
}

