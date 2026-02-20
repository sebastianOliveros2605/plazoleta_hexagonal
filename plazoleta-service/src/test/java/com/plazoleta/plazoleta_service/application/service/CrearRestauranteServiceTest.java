package com.plazoleta.plazoleta_service.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.plazoleta_service.domain.exception.RolUsuarioNoPermitidoException;
import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class)
class CrearRestauranteServiceTest {

    @Mock
    private IRestauranteRepositoryPort restauranteRepository;

    @Mock
    private IUsuarioClientPort usuarioClientPort;

    @InjectMocks
    private CrearRestauranteService crearRestauranteService;

    @Test
    void ejecutar_CuandoDatosValidos_DeberiaGuardar() {

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(5);
        restaurante.setNombre("Restaurante 1");
        restaurante.setNit("123456");
        restaurante.setTelefono("3001234567");

        when(usuarioClientPort.existeUsuario(5)).thenReturn(true);
        when(usuarioClientPort.rolUsuarioString(5)).thenReturn("PROPIETARIO");

        crearRestauranteService.ejecutar(restaurante);

        verify(restauranteRepository).guardar(restaurante);
    }

    @Test
    void ejecutar_SiUsuarioNoExiste_DebeLanzarUsuarioNoExisteException() {

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(5);

        when(usuarioClientPort.existeUsuario(5)).thenReturn(false);

        assertThrows(UsuarioNoExisteException.class,
                () -> crearRestauranteService.ejecutar(restaurante));

        verify(restauranteRepository, never()).guardar(any());
    }

    @Test
    void ejecutar_SiRolNoEsPropietario_DebeLanzarRolUsuarioNoPermitidoException() {

        Restaurante restaurante = new Restaurante();
        restaurante.setIdPropietario(5);

        when(usuarioClientPort.existeUsuario(5)).thenReturn(true);
        when(usuarioClientPort.rolUsuarioString(5)).thenReturn("ADMIN");

        assertThrows(RolUsuarioNoPermitidoException.class,
                () -> crearRestauranteService.ejecutar(restaurante));

        verify(restauranteRepository, never()).guardar(any());
    }

}