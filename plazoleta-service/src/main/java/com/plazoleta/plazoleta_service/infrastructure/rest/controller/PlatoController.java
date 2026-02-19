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
import com.plazoleta.plazoleta_service.domain.ports.out.IModificarPlatoUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.PlatoRestMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final ICrearPlatoUseCase crearPlatoUseCase;
    private final IModificarPlatoUseCase modificarPlatoUseCase;

   //@PreAuthorize("hasRole('PROPIETARIO')")
    @PostMapping("/crearPlato")
    public ResponseEntity<Void> crearPlato(
            @Valid @RequestBody PlatoRequestDTO request,
            Authentication authentication) {
            System.out.println("ENTRA A CREAR PLATO");
        Integer propietarioId = Integer.parseInt(authentication.getName());
        crearPlatoUseCase.crearPlato(
                PlatoRestMapper.toDomain(request),
                propietarioId
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('PROPIETARIO')")
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Void> modificarPlato(
            @PathVariable Long id,
            @Valid @RequestBody PlatoRequestDTO request,
            Authentication authentication) {

        Integer propietarioId = Integer.parseInt(authentication.getName());

        Plato plato = PlatoRestMapper.toDomain(request);
        plato.setId(id);
        System.out.println("ID DEL PLATO A MOD EN CONTLL: "+plato.getId());
        modificarPlatoUseCase.modificarPlato(plato, propietarioId);

        return ResponseEntity.ok().build();
    }
}
