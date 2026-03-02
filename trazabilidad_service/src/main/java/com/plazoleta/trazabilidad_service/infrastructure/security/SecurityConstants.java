package com.plazoleta.trazabilidad_service.infrastructure.security;

import com.plazoleta.trazabilidad_service.domain.constants.RoleConstants;

public final class SecurityConstants {

    public static final String PREFIJO_ROL = "ROLE_";
    public static final String ENCABEZADO_AUTORIZACION = "Authorization";
    public static final String PREFIJO_BEARER = "Bearer ";

    public static final String TIENE_ROL_ADMIN = "hasRole('" + RoleConstants.ROL_ADMIN + "')";
    public static final String TIENE_ROL_PROPIETARIO = "hasRole('" + RoleConstants.ROL_PROPIETARIO + "')";
    public static final String TIENE_ROL_CLIENTE = "hasRole('" + RoleConstants.ROL_CLIENTE + "')";
    public static final String TIENE_ROL_EMPLEADO = "hasRole('" + RoleConstants.ROL_EMPLEADO + "')";

    private SecurityConstants() {
    }
}
