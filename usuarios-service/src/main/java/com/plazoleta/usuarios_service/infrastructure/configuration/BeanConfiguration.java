package com.plazoleta.usuarios_service.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.plazoleta.usuarios_service.application.useCase.ConsultarUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.CrearEmpleadoUseCase;
import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.LoginUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IRestauranteClientePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter.UsuarioJpaAdapter;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.mapper.UsuarioMapper;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.RolJpaRepository;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.UsuarioJpaRepository;
import com.plazoleta.usuarios_service.infrastructure.security.PasswordEncoderAdapter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final RolJpaRepository rolJpaRepository;

    @Bean
    public IUsuarioPersistencePort usuarioPersistencePort(UsuarioMapper usuarioMapper) {
        return new UsuarioJpaAdapter(usuarioJpaRepository, rolJpaRepository, usuarioMapper);
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
            IPasswordEncoderPort passwordEncoderPort) {

        return new LoginUsuarioUseCase(
                usuarioPersistencePort,
                passwordEncoderPort);
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

    @Bean
    public CrearEmpleadoUseCase crearEmpleadoUseCase(
            CrearUsuarioUseCase crearUsuarioUseCase,
            IRestauranteClientePort restauranteClientePort) {
        return new CrearEmpleadoUseCase(crearUsuarioUseCase, restauranteClientePort);
    }
}
