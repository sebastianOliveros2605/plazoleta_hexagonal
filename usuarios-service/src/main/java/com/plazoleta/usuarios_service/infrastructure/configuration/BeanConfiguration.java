package com.plazoleta.usuarios_service.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.plazoleta.usuarios_service.application.useCase.ConsultarUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.LoginUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter.UsuarioJpaAdapter;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.UsuarioJpaRepository;
import com.plazoleta.usuarios_service.infrastructure.security.JwtService;
import com.plazoleta.usuarios_service.infrastructure.security.PasswordEncoderAdapter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final UsuarioJpaRepository usuarioJpaRepository;

    @Bean
    public IUsuarioPersistencePort usuarioPersistencePort() {
        return new UsuarioJpaAdapter(usuarioJpaRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CrearUsuarioUseCase crearUsuarioUseCase(
            IUsuarioPersistencePort usuarioPersistencePort,
            IPasswordEncoderPort passwordEncoderPort) {
        return new CrearUsuarioUseCase(usuarioPersistencePort, passwordEncoderPort);
    }

    @Bean
    public LoginUsuarioUseCase loginUsuarioUseCase(
            IUsuarioPersistencePort usuarioPersistencePort,
            IPasswordEncoderPort passwordEncoderPort,
            JwtService jwtService) {

        return new LoginUsuarioUseCase(
                usuarioPersistencePort,
                passwordEncoderPort,
                jwtService);
    }

    @Bean
    public IPasswordEncoderPort passwordEncoderPort(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderAdapter(passwordEncoder);
    }
    
    @Bean
    public ConsultarUsuarioUseCase consultarUsuarioUseCase(
            IUsuarioPersistencePort usuarioPersistencePort) {
        return new ConsultarUsuarioUseCase(usuarioPersistencePort);
    }
}
