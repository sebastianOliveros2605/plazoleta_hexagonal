package com.plazoleta.usuarios_service.domain.exception;

public class RolNoEncontradoException extends RuntimeException {
    public RolNoEncontradoException(Integer idRol) {
        super("Rol no encontrado para id: " + idRol);
    }
}

