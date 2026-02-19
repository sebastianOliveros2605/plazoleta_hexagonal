package com.plazoleta.plazoleta_service.infrastructure.rest.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private Boolean activo;
}

