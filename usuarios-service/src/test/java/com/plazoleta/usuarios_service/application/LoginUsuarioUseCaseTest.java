package com.plazoleta.usuarios_service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.usuarios_service.application.dto.LoginCommand;
import com.plazoleta.usuarios_service.application.dto.LoginResult;
import com.plazoleta.usuarios_service.application.useCase.LoginUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios_service.domain.exception.UsuarioNoEncontradoException;
import com.plazoleta.usuarios_service.domain.model.RolNombre;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;

@ExtendWith(MockitoExtension.class)
class LoginUsuarioUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private LoginUsuarioUseCase loginUsuarioUseCase;

    @Test
    void login_CuandoCredencialesValidas_DeberiaRetornarDatosAutenticados() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo("test@mail.com");
        usuario.setPassword("hash");
        usuario.setRol(RolNombre.PROPIETARIO);

        when(usuarioPersistencePort.findByCorreo("test@mail.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.matches("1234", "hash")).thenReturn(true);

        LoginResult result = loginUsuarioUseCase.login(new LoginCommand("test@mail.com", "1234"));

        assertEquals(1, result.idUsuario());
        assertEquals("test@mail.com", result.correo());
        assertEquals("PROPIETARIO", result.rol());
    }

    @Test
    void login_CuandoUsuarioNoExiste_DeberiaFallar() {
        when(usuarioPersistencePort.findByCorreo("none@mail.com")).thenReturn(Optional.empty());

        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> loginUsuarioUseCase.login(new LoginCommand("none@mail.com", "1234")));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void login_CuandoPasswordNoCoincide_DeberiaFallar() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@mail.com");
        usuario.setPassword("hash");

        when(usuarioPersistencePort.findByCorreo("test@mail.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.matches("bad", "hash")).thenReturn(false);

        CredencialesInvalidasException exception = assertThrows(
                CredencialesInvalidasException.class,
                () -> loginUsuarioUseCase.login(new LoginCommand("test@mail.com", "bad")));

        assertEquals("Credenciales invalidas", exception.getMessage());
    }
}
