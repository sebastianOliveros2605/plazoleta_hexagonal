package com.plazoleta.usuarios_service.domain.exception;

public class RolNoEncontradoException extends RuntimeException {
    public RolNoEncontradoException(String nombreRol) {
        super("Rol no encontrado para nombre: " + nombreRol);
    }
}
