package com.plazoleta.plazoleta_service.infrastructure.rest;

import com.plazoleta.plazoleta_service.infrastructure.security.SecurityConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plazoleta")

public class PlazoletaController {
    @GetMapping("/test")
    @PreAuthorize(SecurityConstants.TIENE_ROL_ADMIN)
    public String test() {
        return "Token valido y rol ADMIN";
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
