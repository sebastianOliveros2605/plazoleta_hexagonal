package com.plazoleta.usuarios_service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.exception.DatosInvalidosException;
import com.plazoleta.usuarios_service.domain.exception.EmailDuplicadoException;
import com.plazoleta.usuarios_service.domain.exception.MenorDeEdadException;
import com.plazoleta.usuarios_service.domain.model.RolNombre;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IEmpleadoRestaurantePersistencePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IRestauranteClientePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IRolPersistencePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioRepositoryPort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @Mock
    private IRolPersistencePort rolPersistencePort;

    @Mock
    private IRestauranteClientePort restauranteClientePort;

    @Mock
    private IEmpleadoRestaurantePersistencePort empleadoRestaurantePersistencePort;

    @InjectMocks
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @Test
    void crearPropietario_CuandoCumpleCriterios_DeberiaPersistirConClaveEncriptada() {
        Usuario usuario = buildPropietarioAdulto();
        when(rolPersistencePort.findIdByNombre(RolNombre.PROPIETARIO.name())).thenReturn(java.util.Optional.of(2));
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(passwordEncoderPort.encode("1234")).thenReturn("bcrypt-hash");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = crearUsuarioUseCase.crearPropietario(usuario);

        assertEquals("bcrypt-hash", resultado.getPassword());
        assertEquals(RolNombre.PROPIETARIO, resultado.getRol());
        verify(usuarioRepositoryPort).save(usuario);
    }

    @Test
    void crearPropietario_CuandoCamposFormatoInvalidos_DeberiaFallar() {
        when(rolPersistencePort.findIdByNombre(RolNombre.PROPIETARIO.name())).thenReturn(java.util.Optional.of(2));

        Usuario celularInvalido = buildPropietarioAdulto();
        celularInvalido.setCelular("abc-123");
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearPropietario(celularInvalido));

        Usuario correoInvalido = buildPropietarioAdulto();
        correoInvalido.setCorreo("correo-invalido");
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearPropietario(correoInvalido));

        Usuario campoObligatorioFaltante = buildPropietarioAdulto();
        campoObligatorioFaltante.setNombre(" ");
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearPropietario(campoObligatorioFaltante));

        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearPropietario_CuandoEsMenorDeEdad_DeberiaFallar() {
        Usuario usuario = buildPropietarioAdulto();
        usuario.setFechaNacimiento(toDate(LocalDate.now().minusYears(17)));
        when(rolPersistencePort.findIdByNombre(RolNombre.PROPIETARIO.name())).thenReturn(java.util.Optional.of(2));
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);

        MenorDeEdadException exception = assertThrows(
                MenorDeEdadException.class,
                () -> crearUsuarioUseCase.crearPropietario(usuario));

        assertEquals("El usuario debe ser mayor de edad", exception.getMessage());
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_CuandoCorreoYaExiste_DeberiaFallar() {
        Usuario usuario = buildPropietarioAdulto();
        when(rolPersistencePort.findIdByNombre(RolNombre.PROPIETARIO.name())).thenReturn(java.util.Optional.of(2));
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(true);

        EmailDuplicadoException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> crearUsuarioUseCase.crearPropietario(usuario));

        assertEquals("El correo ya esta registrado", exception.getMessage());
        verify(passwordEncoderPort, never()).encode(any(String.class));
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearCliente_CuandoEsMenorDeEdad_DeberiaPersistir() {
        Usuario usuario = buildClienteMenorEdad();
        when(rolPersistencePort.findIdByNombre(RolNombre.CLIENTE.name())).thenReturn(java.util.Optional.of(4));
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(passwordEncoderPort.encode(usuario.getPassword())).thenReturn("bcrypt-hash");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = crearUsuarioUseCase.crearCliente(usuario);

        assertEquals(RolNombre.CLIENTE, resultado.getRol());
        assertEquals("bcrypt-hash", resultado.getPassword());
        verify(usuarioRepositoryPort).save(usuario);
    }

    @Test
    void crearEmpleado_CuandoPropietarioTieneRestaurante_DeberiaPersistirYGuardarRelacion() {
        Usuario usuario = buildEmpleado();
        when(rolPersistencePort.findIdByNombre(RolNombre.EMPLEADO.name())).thenReturn(java.util.Optional.of(3));
        when(restauranteClientePort.consultarIdRestaurantePorPropietario(12)).thenReturn(99L);
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(passwordEncoderPort.encode(usuario.getPassword())).thenReturn("bcrypt-hash");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(10);
            return u;
        });

        Usuario resultado = crearUsuarioUseCase.crearEmpleado(usuario, 12);

        assertEquals(RolNombre.EMPLEADO, resultado.getRol());
        verify(empleadoRestaurantePersistencePort).saveOrUpdate(10, 99L);
    }

    @Test
    void crearEmpleado_CuandoNoHayRestaurante_DeberiaFallar() {
        Usuario usuario = buildEmpleado();
        when(rolPersistencePort.findIdByNombre(RolNombre.EMPLEADO.name())).thenReturn(java.util.Optional.of(3));
        when(restauranteClientePort.consultarIdRestaurantePorPropietario(12)).thenReturn(null);
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);

        DatosInvalidosException exception = assertThrows(
                DatosInvalidosException.class,
                () -> crearUsuarioUseCase.crearEmpleado(usuario, 12));

        assertEquals("El empleado debe quedar asociado a un restaurante", exception.getMessage());
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_CuandoRolNoExisteEnPersistencia_DeberiaFallar() {
        Usuario usuario = buildPropietarioAdulto();
        when(rolPersistencePort.findIdByNombre(RolNombre.PROPIETARIO.name())).thenReturn(java.util.Optional.empty());

        assertThrows(com.plazoleta.usuarios_service.domain.exception.RolNoEncontradoException.class,
                () -> crearUsuarioUseCase.crearPropietario(usuario));
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    private Usuario buildPropietarioAdulto() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Sebastian");
        usuario.setApellido("Lopez");
        usuario.setDocumentoIdentidad(1234567890L);
        usuario.setCelular("+573001234567");
        usuario.setFechaNacimiento(toDate(LocalDate.now().minusYears(25)));
        usuario.setCorreo("sebastian@mail.com");
        usuario.setPassword("1234");
        return usuario;
    }

    private Usuario buildClienteMenorEdad() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        usuario.setApellido("Torres");
        usuario.setDocumentoIdentidad(7654321L);
        usuario.setCelular("+573001111111");
        usuario.setFechaNacimiento(toDate(LocalDate.now().minusYears(17)));
        usuario.setCorreo("ana@mail.com");
        usuario.setPassword("abc123");
        return usuario;
    }

    private Usuario buildEmpleado() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Carlos");
        usuario.setApellido("Diaz");
        usuario.setDocumentoIdentidad(99887766L);
        usuario.setCelular("+573001234567");
        usuario.setFechaNacimiento(toDate(LocalDate.now().minusYears(22)));
        usuario.setCorreo("carlos@mail.com");
        usuario.setPassword("pass123");
        return usuario;
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
