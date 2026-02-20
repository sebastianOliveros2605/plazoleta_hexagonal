package com.plazoleta.plazoleta_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Restaurante {
    private Long id;
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private Integer idPropietario;

    public Restaurante(String nombre, String nit, String direccion,
            String telefono, String urlLogo, Integer idPropietario) {

        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.urlLogo = urlLogo;
        this.idPropietario = idPropietario;
    }

    public Restaurante(){}

}
