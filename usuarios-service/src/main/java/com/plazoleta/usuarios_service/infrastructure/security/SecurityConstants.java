package com.plazoleta.usuarios_service.infrastructure.security;

import com.plazoleta.usuarios_service.domain.model.Rol;

public final class SecurityConstants {

    public static final String PREFIJO_ROL = "ROLE_";
    public static final String ENCABEZADO_AUTORIZACION = "Authorization";
    public static final String PREFIJO_BEARER = "Bearer ";

    public static final String JWT_CLAIM_IDENTIFICADOR = "id";
    public static final String JWT_CLAIM_ROL = "role";

    public static final String TIENE_ROL_ADMIN = "hasRole('" + Rol.ADMIN + "')";
    public static final String TIENE_ROL_PROPIETARIO = "hasRole('" + Rol.PROPIETARIO + "')";

    private SecurityConstants() {
    }
}
