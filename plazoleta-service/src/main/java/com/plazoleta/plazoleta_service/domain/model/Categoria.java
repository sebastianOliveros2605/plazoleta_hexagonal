package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Categoria {
    private Integer id;
    private String nombre;
    private String descripcion;

    public void normalizarNombre() {
        this.nombre=this.nombre.toUpperCase();
        }

}
