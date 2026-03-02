package com.plazoleta.plazoleta_service.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModificarPlatoRequestDTO {

    @Schema(example = "Descripcion actualizada del plato")
    @NotBlank
    private String descripcion;

    @Schema(example = "28000")
    @NotNull
    @Positive
    private Integer precio;
}
