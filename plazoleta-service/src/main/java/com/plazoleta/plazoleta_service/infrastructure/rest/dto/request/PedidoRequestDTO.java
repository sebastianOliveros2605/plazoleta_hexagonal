package com.plazoleta.plazoleta_service.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class PedidoRequestDTO {
    @Schema(example = "1")
    @NotNull
    private Long idRestaurante;
    private Integer idCliente;
    @Schema(example = "[idPlato:2" +
            ",catidad:2]")
    @NotEmpty
    @Valid
    private List<DetallePedidoRequestDTO> detallePedido;

}
