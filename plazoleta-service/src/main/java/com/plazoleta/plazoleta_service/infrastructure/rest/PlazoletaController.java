package com.plazoleta.plazoleta_service.infrastructure.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.plazoleta_service.infrastructure.security.SecurityConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/plazoleta")
@Tag(name = "Plazoleta Debug", description = "Endpoints de prueba y verificacion")
public class PlazoletaController {
    @Operation(
            summary = "Endpoint de prueba para ADMIN",
            description = "Valida que el token sea correcto y el rol sea ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/test")
    @PreAuthorize(SecurityConstants.TIENE_ROL_ADMIN)
    public String test() {
        return "Token valido y rol ADMIN";
    }

    @Operation(
            summary = "Ver autoridades autenticadas",
            description = "Retorna authorities del token actual.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/debug")
    public Object debug(Authentication authentication) {
        return authentication.getAuthorities();
    }

    @Operation(summary = "Health de prueba", description = "Endpoint publico de validacion rapida.")
    @GetMapping("/test2")
    public String test2() {
        return "Funciona";
    }
}
