package com.plazoleta.plazoleta_service.infrastructure.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntregarPedidoRequestDTO {

    @NotBlank
    private String pinSeguridad;
}
