package com.plazoleta.plazoleta_service.infrastructure.rest.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearRestauranteRequest {
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private Integer idPropietario;
}
