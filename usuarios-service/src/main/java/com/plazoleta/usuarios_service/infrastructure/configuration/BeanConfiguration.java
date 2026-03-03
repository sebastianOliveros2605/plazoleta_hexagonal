package com.plazoleta.usuarios_service.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.plazoleta.usuarios_service.application.useCase.ConsultarUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.LoginUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.puertosIn.IEmpleadoRestaurantePersistencePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IRestauranteClientePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IRolPersistencePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter.EmpleadoRestauranteJpaAdapter;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter.RolJpaAdapter;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter.UsuarioJpaAdapter;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.EmpleadoRestauranteJpaRepository;
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
    private final EmpleadoRestauranteJpaRepository empleadoRestauranteJpaRepository;

    @Bean
    public IUsuarioPersistencePort usuarioPersistencePort(UsuarioMapper usuarioMapper) {
        return new UsuarioJpaAdapter(usuarioJpaRepository, usuarioMapper);
    }

    @Bean
    public IRolPersistencePort rolPersistencePort() {
        return new RolJpaAdapter(rolJpaRepository);
    }

    @Bean
    public IEmpleadoRestaurantePersistencePort empleadoRestaurantePersistencePort() {
        return new EmpleadoRestauranteJpaAdapter(empleadoRestauranteJpaRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CrearUsuarioUseCase crearUsuarioUseCase(
            IUsuarioPersistencePort usuarioPersistencePort,
            IRolPersistencePort rolPersistencePort,
            IRestauranteClientePort restauranteClientePort,
            IEmpleadoRestaurantePersistencePort empleadoRestaurantePersistencePort,
            IPasswordEncoderPort passwordEncoderPort) {
        return new CrearUsuarioUseCase(
                usuarioPersistencePort,
                rolPersistencePort,
                restauranteClientePort,
                empleadoRestaurantePersistencePort,
                passwordEncoderPort);
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

}
