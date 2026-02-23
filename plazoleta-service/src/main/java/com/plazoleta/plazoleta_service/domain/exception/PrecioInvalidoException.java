package com.plazoleta.plazoleta_service.domain.exception;

public class PrecioInvalidoException extends RuntimeException {

    public PrecioInvalidoException() {
        super("El precio debe ser un numero entero positivo.");
    }
}
