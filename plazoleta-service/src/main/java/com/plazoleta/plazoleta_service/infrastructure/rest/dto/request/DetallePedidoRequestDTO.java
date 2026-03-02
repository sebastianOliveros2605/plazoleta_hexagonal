package com.plazoleta.plazoleta_service.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedidoRequestDTO {
    @Schema(example = "1")
    @NotNull
    private Long idPlato;
    @Schema(example = "2")
    @NotNull
    @Positive
    private Integer cantidad;
}
