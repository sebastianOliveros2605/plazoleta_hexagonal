package com.plazoleta.plazoleta_service.domain.exception;

public class CampoObligatorioException extends RuntimeException {

    public CampoObligatorioException(String campo) {
        super("El campo " + campo + " es obligatorio.");
    }
}
