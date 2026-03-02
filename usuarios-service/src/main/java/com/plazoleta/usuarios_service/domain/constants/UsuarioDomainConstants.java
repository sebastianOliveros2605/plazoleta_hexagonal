package com.plazoleta.usuarios_service.domain.constants;

public final class UsuarioDomainConstants {

    public static final String REGEX_TELEFONO = "^\\+?[0-9]{1,13}$";
    public static final String REGEX_CORREO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final int EDAD_MINIMA = 18;

    public static final String MENSAJE_CAMPOS_OBLIGATORIOS = "Todos los campos obligatorios deben estar completos";
    public static final String MENSAJE_CELULAR_INVALIDO = "El celular debe ser numerico y de maximo 13 caracteres";
    public static final String MENSAJE_CORREO_INVALIDO = "El correo no es valido";
    public static final String MENSAJE_ROL_OBLIGATORIO = "El rol es obligatorio";
    public static final String MENSAJE_FECHA_NACIMIENTO_OBLIGATORIA = "La fecha de nacimiento es obligatoria para este rol";
    public static final String MENSAJE_EMPLEADO_SIN_RESTAURANTE = "El empleado debe quedar asociado a un restaurante";

    private UsuarioDomainConstants() {
    }
}
