package com.plazoleta.usuarios_service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;

@ExtendWith(MockitoExtension.class)
public class CrearUsuarioUseCaseTest {
    @Mock
    private IUsuarioPersistencePort usuarioRepositoryPort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @Test
    void crearUsuario_CuandoDatosValidos_DeberiaGuardar() {

        Usuario usuario = new Usuario();
        usuario.setCorreo("test@test.com");
        usuario.setDocumentoIdentidad(123456L);
        usuario.setCelular("+573001234567");

        when(passwordEncoderPort.encode("1234"))
                .thenReturn("hashed");

        crearUsuarioUseCase.crearUsuario(usuario);

        verify(usuarioRepositoryPort).save(usuario);
        assertEquals("hashed", usuario.getPassword());
    }

}
