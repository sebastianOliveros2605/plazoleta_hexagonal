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
public class PlatoRequestDTO {

    @Schema(example = "Hamburguesa Doble")
    @NotBlank
    private String nombre;
    @Schema(example = "Hamburguesa con doble carne y queso cheddar")
    @NotBlank
    private String descripcion;
    @Schema(example = "25000")
    @NotNull
    @Positive
    private Integer precio;
    @Schema(example = "https://imagenes.com/hamburguesa.jpg")
    @NotBlank
    private String urlImagen;
    @Schema(example = "1")
    @NotNull
    private Long idRestaurante;
    @Schema(example = "1")
    @NotNull
    private Long idCategoria;
}
