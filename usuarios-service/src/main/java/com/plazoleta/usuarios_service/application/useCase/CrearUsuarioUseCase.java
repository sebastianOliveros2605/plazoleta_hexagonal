package com.plazoleta.usuarios_service.application.useCase;

import com.plazoleta.usuarios_service.domain.exception.DatosInvalidosException;
import com.plazoleta.usuarios_service.domain.exception.EmailDuplicadoException;
import com.plazoleta.usuarios_service.domain.exception.MenorDeEdadException;
import com.plazoleta.usuarios_service.domain.constants.UsuarioDomainConstants;
import com.plazoleta.usuarios_service.domain.model.Rol;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class CrearUsuarioUseCase {
    private static final Pattern PHONE_PATTERN = Pattern.compile(UsuarioDomainConstants.REGEX_TELEFONO);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(UsuarioDomainConstants.REGEX_CORREO);

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public Usuario crearUsuario(Usuario usuario) {
        validarCamposObligatorios(usuario);
        validarCelular(usuario.getCelular());
        validarCorreo(usuario.getCorreo());
        validarRol(usuario);
        validarAsociacionRestaurantePorRol(usuario);
        validarCorreoUnico(usuario.getCorreo());

        if (Rol.ADMIN.equals(usuario.getRol().getNombre()) || Rol.PROPIETARIO.equals(usuario.getRol().getNombre())) {
            validarMayorDeEdad(usuario);
        }

        String passwordHashed = passwordEncoderPort.encode(usuario.getPassword());
        usuario.setPassword(passwordHashed);

        return usuarioPersistencePort.save(usuario);
    }

    private void validarAsociacionRestaurantePorRol(Usuario usuario) {
        if (Rol.EMPLEADO.equals(usuario.getRol().getNombre()) && usuario.getIdRestaurante() == null) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_EMPLEADO_SIN_RESTAURANTE);
        }

        if (!Rol.EMPLEADO.equals(usuario.getRol().getNombre())) {
            usuario.setIdRestaurante(null);
        }
    }

    private void validarCamposObligatorios(Usuario usuario) {
        if (isBlank(usuario.getNombre())
                || isBlank(usuario.getApellido())
                || usuario.getDocumentoIdentidad() == null
                || isBlank(usuario.getCelular())
                || usuario.getFechaNacimiento() == null
                || isBlank(usuario.getCorreo())
                || isBlank(usuario.getPassword())) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_CAMPOS_OBLIGATORIOS);
        }
    }

    private void validarCelular(String celular) {
        if (!PHONE_PATTERN.matcher(celular).matches()) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_CELULAR_INVALIDO);
        }
    }

    private void validarCorreo(String correo) {
        if (!EMAIL_PATTERN.matcher(correo).matches()) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_CORREO_INVALIDO);
        }
    }

    private void validarRol(Usuario usuario) {
        if (usuario.getRol() == null || usuario.getRol().getId() == null || isBlank(usuario.getRol().getNombre())) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_ROL_OBLIGATORIO);
        }
    }

    private void validarCorreoUnico(String correo) {
        if (usuarioPersistencePort.existsByCorreo(correo)) {
            throw new EmailDuplicadoException();
        }
    }

    private void validarMayorDeEdad(Usuario usuario) {
        if (usuario.getFechaNacimiento() == null) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_FECHA_NACIMIENTO_OBLIGATORIA);
        }

        LocalDate fechaNacimiento = new java.sql.Date(usuario.getFechaNacimiento().getTime()).toLocalDate();
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if (edad < UsuarioDomainConstants.EDAD_MINIMA) {
            throw new MenorDeEdadException();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
