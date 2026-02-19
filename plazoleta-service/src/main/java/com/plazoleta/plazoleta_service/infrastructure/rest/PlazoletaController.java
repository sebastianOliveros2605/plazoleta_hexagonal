package com.plazoleta.plazoleta_service.infrastructure.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plazoleta")
public class PlazoletaController {
    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String test() {
        return "Token válido y rol ADMIN";
    }

    @GetMapping("/debug")
    public Object debug(Authentication authentication) {
        return authentication.getAuthorities();
    }

    @GetMapping("/test2")
    public String test2() {
        return "Funciona";
    }

}
