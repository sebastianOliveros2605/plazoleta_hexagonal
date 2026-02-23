package com.plazoleta.usuarios_service.infrastructure.input.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(example = "usuario@mail.com")
        @NotBlank @Email String correo,
        @Schema(example = "Secreto123*")
        @NotBlank String password) {
}

