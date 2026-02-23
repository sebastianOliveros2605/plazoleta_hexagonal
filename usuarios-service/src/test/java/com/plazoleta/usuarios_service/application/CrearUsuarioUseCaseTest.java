package com.plazoleta.usuarios_service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.plazoleta.usuarios_service.domain.model.Rol;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioRepositoryPort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @Test
    void crearPropietario_CuandoCumpleCriterios_DeberiaPersistirConClaveEncriptada() {
        Usuario usuario = buildPropietarioAdulto();
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(passwordEncoderPort.encode("1234")).thenReturn("bcrypt-hash");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = crearUsuarioUseCase.crearUsuario(usuario);

        assertEquals("bcrypt-hash", resultado.getPassword());
        assertEquals(Rol.PROPIETARIO, resultado.getRol().getNombre());
        verify(usuarioRepositoryPort).save(usuario);
    }

    @Test
    void crearPropietario_CuandoCamposFormatoInvalidos_DeberiaFallar() {
        Usuario celularInvalido = buildPropietarioAdulto();
        celularInvalido.setCelular("abc-123");
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearUsuario(celularInvalido));

        Usuario correoInvalido = buildPropietarioAdulto();
        correoInvalido.setCorreo("correo-invalido");
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearUsuario(correoInvalido));

        Usuario campoObligatorioFaltante = buildPropietarioAdulto();
        campoObligatorioFaltante.setNombre(" ");
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearUsuario(campoObligatorioFaltante));

        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearPropietario_CuandoEsMenorDeEdad_DeberiaFallar() {
        Usuario usuario = buildPropietarioAdulto();
        usuario.setFechaNacimiento(toDate(LocalDate.now().minusYears(17)));
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);

        MenorDeEdadException exception = assertThrows(
                MenorDeEdadException.class,
                () -> crearUsuarioUseCase.crearUsuario(usuario));

        assertEquals("El usuario debe ser mayor de edad", exception.getMessage());
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_CuandoCorreoYaExiste_DeberiaFallar() {
        Usuario usuario = buildPropietarioAdulto();
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(true);

        EmailDuplicadoException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> crearUsuarioUseCase.crearUsuario(usuario));

        assertEquals("El correo ya esta registrado", exception.getMessage());
        verify(passwordEncoderPort, never()).encode(any(String.class));
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_CuandoRolNoEsObligadoMayorEdad_DeberiaPersistir() {
        Usuario usuario = buildClienteMenorEdad();
        usuario.setIdRestaurante(55L);
        when(usuarioRepositoryPort.existsByCorreo(usuario.getCorreo())).thenReturn(false);
        when(passwordEncoderPort.encode(usuario.getPassword())).thenReturn("bcrypt-hash");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = crearUsuarioUseCase.crearUsuario(usuario);

        assertEquals("CLIENTE", resultado.getRol().getNombre());
        assertNull(resultado.getIdRestaurante());
        assertEquals("bcrypt-hash", resultado.getPassword());
        verify(usuarioRepositoryPort).save(usuario);
    }

    @Test
    void crearUsuario_CuandoEmpleadoSinRestaurante_DeberiaFallar() {
        Usuario usuario = buildEmpleado();
        usuario.setIdRestaurante(null);

        DatosInvalidosException exception = assertThrows(
                DatosInvalidosException.class,
                () -> crearUsuarioUseCase.crearUsuario(usuario));

        assertEquals("El empleado debe quedar asociado a un restaurante", exception.getMessage());
        verify(usuarioRepositoryPort, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_CuandoRolInvalido_DeberiaFallar() {
        Usuario rolSinId = buildPropietarioAdulto();
        rolSinId.setRol(new Rol(null, Rol.PROPIETARIO, null));
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearUsuario(rolSinId));

        Usuario rolSinNombre = buildPropietarioAdulto();
        rolSinNombre.setRol(new Rol(Rol.PROPIETARIO_ID, " ", null));
        assertThrows(DatosInvalidosException.class, () -> crearUsuarioUseCase.crearUsuario(rolSinNombre));

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
        usuario.setRol(new Rol(Rol.PROPIETARIO_ID, Rol.PROPIETARIO, null));
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
        usuario.setRol(new Rol(Rol.CLIENTE_ID, Rol.CLIENTE, null));
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
        usuario.setRol(new Rol(Rol.EMPLEADO_ID, Rol.EMPLEADO, null));
        return usuario;
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
