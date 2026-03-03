package com.plazoleta.usuarios_service.application.useCase;

import com.plazoleta.usuarios_service.application.dto.LoginCommand;
import com.plazoleta.usuarios_service.application.dto.LoginResult;
import com.plazoleta.usuarios_service.domain.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios_service.domain.exception.UsuarioNoEncontradoException;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginUsuarioUseCase {

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public LoginResult login(LoginCommand request) {
        Usuario usuario = usuarioPersistencePort.findByCorreo(request.correo())
                .orElseThrow(UsuarioNoEncontradoException::new);

        if (!passwordEncoderPort.matches(request.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException();
        }

        return new LoginResult(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol().name());
    }
}
