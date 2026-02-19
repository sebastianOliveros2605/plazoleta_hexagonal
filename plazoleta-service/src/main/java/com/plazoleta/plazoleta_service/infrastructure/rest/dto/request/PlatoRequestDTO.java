package com.plazoleta.plazoleta_service.infrastructure.rest.dto.request;

import com.plazoleta.plazoleta_service.domain.model.CategoriaPlatoEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlatoRequestDTO {

    private String nombre;
    private String descripcion;
    @Positive
    private Integer precio;
    private Long idRestaurante;
    private CategoriaPlatoEnum categoria;
}
