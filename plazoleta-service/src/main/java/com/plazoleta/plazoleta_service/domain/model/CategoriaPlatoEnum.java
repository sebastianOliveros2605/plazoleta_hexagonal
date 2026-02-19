package com.plazoleta.plazoleta_service.domain.model;

public enum CategoriaPlatoEnum {
    PLATO_FUERTE("Plato furte"),
    POSTRE("Postre"),
    ENTRADA("Plato de entrada"),
    BEBIDA("Bebida");

    private final String descripcion;
    // El constructor de un enum siempre es privado por defecto
    CategoriaPlatoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
