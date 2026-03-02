package com.plazoleta.plazoleta_service.domain.exception;

public class UsuarioNoAsociadoRestauranteException extends RuntimeException {

    public UsuarioNoAsociadoRestauranteException() {
        super("El usuario no esta asociado a ningun restaurante.");
    }
}
