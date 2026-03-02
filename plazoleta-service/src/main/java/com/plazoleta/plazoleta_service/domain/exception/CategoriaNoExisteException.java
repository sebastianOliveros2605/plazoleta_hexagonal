package com.plazoleta.plazoleta_service.domain.exception;

public class CategoriaNoExisteException extends RuntimeException {

    public CategoriaNoExisteException() {
        super("La Categoria no existe.");
    }
}
