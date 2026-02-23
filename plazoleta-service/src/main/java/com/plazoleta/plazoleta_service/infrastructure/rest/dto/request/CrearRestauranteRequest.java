package com.plazoleta.plazoleta_service.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearRestauranteRequest {
    @Schema(example = "La Parrilla Central")
    @NotBlank
    private String nombre;
    @Schema(example = "900123456-7")
    @NotBlank
    private String nit;
    @Schema(example = "Calle 10 # 20-30")
    @NotBlank
    private String direccion;
    @Schema(example = "+573105554433")
    @NotBlank
    private String telefono;
    @Schema(example = "https://imagenes.com/logo-restaurante.png")
    @NotBlank
    private String urlLogo;
    @Schema(example = "5")
    @NotNull
    private Integer idPropietario;
}
