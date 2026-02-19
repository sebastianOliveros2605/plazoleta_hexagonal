package com.plazoleta.plazoleta_service.domain.exception;

public class PlatoNoExisteException extends RuntimeException {

    public PlatoNoExisteException() {
        super("El plato no existe.");
    }

}
