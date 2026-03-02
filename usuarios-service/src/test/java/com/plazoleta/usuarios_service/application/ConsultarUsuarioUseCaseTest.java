package com.plazoleta.usuarios_service.application;

import com.plazoleta.usuarios_service.application.useCase.ConsultarUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.exception.UsuarioNoEncontradoException;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarUsuarioUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @InjectMocks
    private ConsultarUsuarioUseCase consultarUsuarioUseCase;

    private static final Integer ID_USUARIO_EXISTENTE = 1;
    private static final Integer ID_USUARIO_INEXISTENTE = 999;

    @Test
    void consultarPorId_CuandoExiste_DeberiaRetornarUsuario() {
        Usuario usuario = new Usuario();
        when(usuarioPersistencePort.findById(ID_USUARIO_EXISTENTE)).thenReturn(Optional.of(usuario));

        Usuario resultado = consultarUsuarioUseCase.consultarPorId(ID_USUARIO_EXISTENTE);

        assertSame(usuario, resultado);
    }

    @Test
    void consultarPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(usuarioPersistencePort.findById(ID_USUARIO_INEXISTENTE)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> consultarUsuarioUseCase.consultarPorId(ID_USUARIO_INEXISTENTE));
    }

    @Test
    void existePorId_DeberiaRetornarEstadoCorrecto() {
        when(usuarioPersistencePort.findById(ID_USUARIO_EXISTENTE)).thenReturn(Optional.of(new Usuario()));
        when(usuarioPersistencePort.findById(ID_USUARIO_INEXISTENTE)).thenReturn(Optional.empty());

        assertTrue(consultarUsuarioUseCase.existePorId(ID_USUARIO_EXISTENTE));
        assertFalse(consultarUsuarioUseCase.existePorId(ID_USUARIO_INEXISTENTE));
    }
}
