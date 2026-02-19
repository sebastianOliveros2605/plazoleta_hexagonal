package com.plazoleta.usuarios_service.application.useCase;

import java.time.LocalDate;
import java.time.Period;

import com.plazoleta.usuarios_service.domain.model.RolEnum;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrearUsuarioUseCase {

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public Usuario crearUsuario(Usuario usuario) {

        validarCorreoUnico(usuario.getCorreo());
        if(usuario.getRol().equals(RolEnum.ADMIN)||usuario.getRol().equals(RolEnum.PROPIETARIO)){
            validarMayorDeEdad(usuario);
        }


        String passwordEncriptada = passwordEncoderPort.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);

        return usuarioPersistencePort.save(usuario);
    }

    private void validarCorreoUnico(String correo) {
        if (usuarioPersistencePort.existsByCorreo(correo)) {
            throw new RuntimeException("El correo ya está registrado");
        }
    }

    private void validarMayorDeEdad(Usuario usuario) {
        if (usuario.getFechaNacimiento() == null) return;

        LocalDate fechaNacimiento = new java.sql.Date(usuario.getFechaNacimiento().getTime()).toLocalDate();
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if (edad < 18) {
            throw new RuntimeException("El usuario debe ser mayor de edad");
        }
    }

}
