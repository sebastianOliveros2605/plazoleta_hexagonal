package com.plazoleta.usuarios_service.domain.exception;

public class RestauranteNoEncontradoException extends RuntimeException {

    public RestauranteNoEncontradoException() {
        super("No se encontro un restaurante asociado al propietario");
    }
}

