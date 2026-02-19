package com.plazoleta.plazoleta_service.domain.exception;

public class NombreInvalidoException extends RuntimeException {

    public NombreInvalidoException() {
        super("El nombre del restaurante no puede estar vacío ni contener solo números.");
    }

}
