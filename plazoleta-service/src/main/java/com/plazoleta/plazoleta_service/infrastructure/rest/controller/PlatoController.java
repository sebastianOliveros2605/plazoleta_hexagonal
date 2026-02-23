package com.plazoleta.plazoleta_service.infrastructure.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.IModificarPlatoUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.ModificarPlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.PlatoRestMapper;
import com.plazoleta.plazoleta_service.infrastructure.security.SecurityConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/platos")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "Operaciones para gestion de platos")
public class PlatoController {

    private final ICrearPlatoUseCase crearPlatoUseCase;
    private final IModificarPlatoUseCase modificarPlatoUseCase;
    private final PlatoRestMapper platoRestMapper;

    @Operation(
            summary = "Crear plato",
            description = "Crea un plato en un restaurante del propietario autenticado.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_PROPIETARIO)
    @PostMapping("/crearPlato")
    public ResponseEntity<Void> crearPlato(
            @Valid @RequestBody PlatoRequestDTO request,
            Authentication authentication) {
        Integer propietarioId = Integer.parseInt(authentication.getName());
        crearPlatoUseCase.crearPlato(
                platoRestMapper.toDomain(request),
                propietarioId
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Modificar plato",
            description = "Actualiza informacion basica de un plato existente.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_PROPIETARIO)
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Void> modificarPlato(
            @PathVariable Long id,
            @Valid @RequestBody ModificarPlatoRequestDTO request,
            Authentication authentication) {

        Integer propietarioId = Integer.parseInt(authentication.getName());

        Plato plato = platoRestMapper.toDomain(request);
        plato.setId(id);
        modificarPlatoUseCase.modificarPlato(plato, propietarioId);

        return ResponseEntity.ok().build();
    }
}
