package com.plazoleta.usuarios_service.domain.model;

public enum RolEnum {
    ADMIN("Admin con poderes de crear perfiles a propietarios."),
    PROPIETARIO("Dueño de la cuenta o establecimiento."),
    EMPLEADO("Personal con acceso limitado."),
    CLIENTE("Usuario final del servicio.");

    private final String descripcion;
    // El constructor de un enum siempre es privado por defecto
    RolEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
