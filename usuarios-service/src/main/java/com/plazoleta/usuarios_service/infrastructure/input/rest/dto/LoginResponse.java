package com.plazoleta.usuarios_service.infrastructure.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicm9sZSI6IkFETUlOIn0.firma")
        String token) {}

