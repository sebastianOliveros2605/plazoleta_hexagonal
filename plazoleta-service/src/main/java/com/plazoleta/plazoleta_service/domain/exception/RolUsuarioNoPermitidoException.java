package com.plazoleta.plazoleta_service.domain.exception;

public class RolUsuarioNoPermitidoException extends RuntimeException {

    public RolUsuarioNoPermitidoException() {
        super("El rol asociado al usuario no tiene permisos para ejecutar la acción");
    }
}
