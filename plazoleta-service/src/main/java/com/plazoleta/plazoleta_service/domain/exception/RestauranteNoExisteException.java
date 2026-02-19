package com.plazoleta.plazoleta_service.domain.exception;

public class RestauranteNoExisteException extends RuntimeException {

    public RestauranteNoExisteException() {
        super("El restaurante no existe.");
    } 

}
