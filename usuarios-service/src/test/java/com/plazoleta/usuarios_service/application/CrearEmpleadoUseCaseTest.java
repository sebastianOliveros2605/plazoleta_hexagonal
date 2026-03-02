package com.plazoleta.usuarios_service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.usuarios_service.application.useCase.CrearEmpleadoUseCase;
import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.exception.RestauranteNoEncontradoException;
import com.plazoleta.usuarios_service.domain.model.Rol;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IRestauranteClientePort;

@ExtendWith(MockitoExtension.class)
class CrearEmpleadoUseCaseTest {

    private static final Integer ID_PROPIETARIO = 12;
    private static final Long ID_RESTAURANTE = 99L;

    @Mock
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @Mock
    private IRestauranteClientePort restauranteClientePort;

    @InjectMocks
    private CrearEmpleadoUseCase crearEmpleadoUseCase;

    @Test
    void crearEmpleado_CuandoPropietarioValido_DeberiaAsignarRolYRestaurante() {
        Usuario empleado = new Usuario();
        empleado.setNombre("Luis");
        empleado.setApellido("Perez");
        empleado.setCorreo("luis@mail.com");
        empleado.setPassword("1234");

        when(restauranteClientePort.consultarIdRestaurantePorPropietario(ID_PROPIETARIO)).thenReturn(ID_RESTAURANTE);
        when(crearUsuarioUseCase.crearUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = crearEmpleadoUseCase.crearEmpleado(empleado, ID_PROPIETARIO);

        assertEquals(Rol.EMPLEADO, resultado.getRol().getNombre());
        assertEquals(ID_RESTAURANTE, resultado.getIdRestaurante());
        verify(restauranteClientePort).consultarIdRestaurantePorPropietario(ID_PROPIETARIO);
        verify(crearUsuarioUseCase).crearUsuario(eq(empleado));
    }

    @Test
    void crearEmpleado_CuandoNoHayRestauranteParaPropietario_DeberiaFallar() {
        Usuario empleado = new Usuario();
        when(restauranteClientePort.consultarIdRestaurantePorPropietario(ID_PROPIETARIO))
                .thenThrow(new RestauranteNoEncontradoException());

        assertThrows(
                RestauranteNoEncontradoException.class,
                () -> crearEmpleadoUseCase.crearEmpleado(empleado, ID_PROPIETARIO));

        verify(crearUsuarioUseCase, never()).crearUsuario(any(Usuario.class));
    }

    @Test
    void crearEmpleado_CuandoRolLlegaAlterado_DeberiaForzarEmpleado() {
        Usuario empleado = new Usuario();
        empleado.setRol(new Rol(Rol.ADMIN_ID, Rol.ADMIN, null));

        when(restauranteClientePort.consultarIdRestaurantePorPropietario(ID_PROPIETARIO)).thenReturn(ID_RESTAURANTE);
        when(crearUsuarioUseCase.crearUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = crearEmpleadoUseCase.crearEmpleado(empleado, ID_PROPIETARIO);

        assertEquals(Rol.EMPLEADO, resultado.getRol().getNombre());
        assertEquals(Rol.EMPLEADO_ID, resultado.getRol().getId());
    }
}
