package com.plazoleta.usuarios_service.application.useCase;

import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.LoginRequest;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.LoginResponse;
import com.plazoleta.usuarios_service.infrastructure.security.JwtService;

import lombok.RequiredArgsConstructor;

import com.plazoleta.usuarios_service.domain.model.Usuario;

import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;

import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;


@RequiredArgsConstructor
public class LoginUsuarioUseCase {

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioPersistencePort.findByCorreo(request.correo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoderPort.matches(request.password(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol().name()
        );

        return new LoginResponse(token);
    }
}
