package com.plazoleta.plazoleta_service.infrastructure.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearRestauranteUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.CrearRestauranteRequest;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.RestauranteRestMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final ICrearRestauranteUseCase crearRestauranteUseCase;

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> crear(@RequestBody CrearRestauranteRequest request) {
        Restaurante restaurante = RestauranteRestMapper.toDomain(request);
        crearRestauranteUseCase.ejecutar(restaurante);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
