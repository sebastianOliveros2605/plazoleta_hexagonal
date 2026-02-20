package com.plazoleta.plazoleta_service.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
class ModificarPlatoServiceTest {

    @Mock
    private IPlatoRepositoryPort platoRepositoryPort;

    @Mock
    private IRestauranteRepositoryPort restauranteRepositoryPort;

    @InjectMocks
    private ModificarPlatoService modificarPlatoService;

    @Test
    void modificarPlato_CuandoDatosValidos_DeberiaGuardar() {
        Plato existente = new Plato();
        existente.setId(1L);
        existente.setIdRestaurante(2L);

        Plato modificado = new Plato();
        modificado.setId(1L);
        modificado.setIdRestaurante(2L);
        modificado.setPrecio(100);

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(7);

        when(platoRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(existente));
        when(restauranteRepositoryPort.buscarPorId(2L))
                .thenReturn(Optional.of(restaurante));

        modificarPlatoService.modificarPlato(modificado, 7);

        verify(platoRepositoryPort).guardar(existente);
    }

    @Test
    void modificarPlato_SiPlatoNoExiste_DebeLanzar() {
        Long id = 99L;
        Plato plato = new Plato();
        plato.setId(id);
        when(platoRepositoryPort.buscarPorId(id))
                .thenReturn(Optional.empty());
        assertThrows(PlatoNoExisteException.class,
                () -> modificarPlatoService.modificarPlato(plato, 7));
    }

    @Test
    void modificarPlato_SiRestauranteNoExiste_DebeLanzar() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setIdRestaurante(2L);

        when(platoRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(plato));
        when(restauranteRepositoryPort.buscarPorId(2L))
                .thenReturn(Optional.empty());

        assertThrows(RestauranteNoExisteException.class,
                () -> modificarPlatoService.modificarPlato(plato, 7));
    }

    @Test
    void modificarPlato_SiNoEsPropietario_DebeLanzar() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setIdRestaurante(2L);

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(8);

        when(platoRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(plato));
        when(restauranteRepositoryPort.buscarPorId(2L))
                .thenReturn(Optional.of(restaurante));

        assertThrows(NoEsPropietarioException.class,
                () -> modificarPlatoService.modificarPlato(plato, 7));
    }

    @Test
    void modificarPlato_SiPrecioInvalido_DebeLanzar() {
        Plato existente = new Plato();
        existente.setId(1L);
        existente.setIdRestaurante(2L);

        Plato modificado = new Plato();
        modificado.setId(1L);
        modificado.setIdRestaurante(2L);
        modificado.setPrecio(0);

        when(platoRepositoryPort.buscarPorId(1L))
                .thenReturn(Optional.of(existente));

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(7);

        when(restauranteRepositoryPort.buscarPorId(2L))
                .thenReturn(Optional.of(restaurante));

        assertThrows(PrecioInvalidoException.class,
                () -> modificarPlatoService.modificarPlato(modificado, 7));
    }
}
