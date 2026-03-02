package com.plazoleta.usuarios_service.infrastructure.input.rest.handler;

import java.time.Instant;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path) {
}

