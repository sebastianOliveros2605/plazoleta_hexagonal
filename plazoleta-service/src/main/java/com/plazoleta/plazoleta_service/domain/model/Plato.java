package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Plato {
    
    private Long id;
    private String nombre; 
    private String descripcion;
    private Integer precio;
    private String urlImagen;
    private CategoriaPlatoEnum categoria;
    private Boolean activo;
    private Long idRestaurante;

    public Plato(String nombre,String descripcion, Integer precio, String urlImagen,CategoriaPlatoEnum categoria, Long idRestaurante){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.categoria = categoria;
        this.idRestaurante = idRestaurante;
        this.activo = true;
    }
    public Plato() {}
}
