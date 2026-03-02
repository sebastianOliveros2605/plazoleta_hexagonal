package com.plazoleta.plazoleta_service.infrastructure.rest.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestauranteResponseDTO {

    private Long id;
    private String nombre;
    private String urlLogo;

}
