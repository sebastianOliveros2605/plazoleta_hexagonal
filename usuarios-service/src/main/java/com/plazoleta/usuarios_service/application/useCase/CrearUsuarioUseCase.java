package com.plazoleta.usuarios_service.application.useCase;

import com.plazoleta.usuarios_service.domain.exception.DatosInvalidosException;
import com.plazoleta.usuarios_service.domain.exception.EmailDuplicadoException;
import com.plazoleta.usuarios_service.domain.exception.MenorDeEdadException;
import com.plazoleta.usuarios_service.domain.exception.RolNoEncontradoException;
import com.plazoleta.usuarios_service.domain.constants.UsuarioDomainConstants;
import com.plazoleta.usuarios_service.domain.model.RolNombre;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IEmpleadoRestaurantePersistencePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;
import com.plazoleta.usuarios_service.domain.puertosIn.IRestauranteClientePort;
import com.plazoleta.usuarios_service.domain.puertosIn.IRolPersistencePort;
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
    private final IRolPersistencePort rolPersistencePort;
    private final IRestauranteClientePort restauranteClientePort;
    private final IEmpleadoRestaurantePersistencePort empleadoRestaurantePersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public Usuario crearPropietario(Usuario usuario) {
        return crearUsuarioConRol(usuario, RolNombre.PROPIETARIO, null);
    }

    public Usuario crearCliente(Usuario usuario) {
        return crearUsuarioConRol(usuario, RolNombre.CLIENTE, null);
    }

    public Usuario crearAdmin(Usuario usuario) {
        return crearUsuarioConRol(usuario, RolNombre.ADMIN, null);
    }

    public Usuario crearEmpleado(Usuario usuario, Integer idPropietarioAutenticado) {
        Long idRestaurante = restauranteClientePort.consultarIdRestaurantePorPropietario(idPropietarioAutenticado);
        return crearUsuarioConRol(usuario, RolNombre.EMPLEADO, idRestaurante);
    }

    private Usuario crearUsuarioConRol(Usuario usuario, RolNombre rolNombre, Long idRestauranteEmpleado) {
        prepararRol(usuario, rolNombre);
        validarCamposObligatorios(usuario);
        validarCelular(usuario.getCelular());
        validarCorreo(usuario.getCorreo());
        validarCorreoUnico(usuario.getCorreo());

        if (esRolAdminOPropietario(usuario.getRol())) {
            validarMayorDeEdad(usuario);
        }

        if (usuario.getRol() == RolNombre.EMPLEADO && idRestauranteEmpleado == null) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_EMPLEADO_SIN_RESTAURANTE);
        }

        String passwordHashed = passwordEncoderPort.encode(usuario.getPassword());
        usuario.setPassword(passwordHashed);

        Usuario usuarioGuardado = usuarioPersistencePort.save(usuario);
        if (rolNombre == RolNombre.EMPLEADO) {
            empleadoRestaurantePersistencePort.saveOrUpdate(usuarioGuardado.getId(), idRestauranteEmpleado);
        }
        return usuarioGuardado;
    }

    private void prepararRol(Usuario usuario, RolNombre rolNombre) {
        if (rolNombre == null) {
            throw new DatosInvalidosException(UsuarioDomainConstants.MENSAJE_ROL_OBLIGATORIO);
        }
        Integer rolId = rolPersistencePort.findIdByNombre(rolNombre.name())
                .orElseThrow(() -> new RolNoEncontradoException(rolNombre.name()));
        usuario.setRolId(rolId);
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

    private boolean esRolAdminOPropietario(RolNombre rolNombre) {
        return rolNombre == RolNombre.ADMIN || rolNombre == RolNombre.PROPIETARIO;
    }
}
