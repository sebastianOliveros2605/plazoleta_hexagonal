package com.plazoleta.plazoleta_service.infrastructure.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarRestaurantePorPropietarioUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearRestauranteUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.CrearRestauranteRequest;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.RestauranteRestMapper;
import com.plazoleta.plazoleta_service.infrastructure.security.SecurityConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Operaciones para gestion de restaurantes")
public class RestauranteController {

    private final ICrearRestauranteUseCase crearRestauranteUseCase;
    private final IConsultarRestaurantePorPropietarioUseCase consultarRestaurantePorPropietarioUseCase;
    private final RestauranteRestMapper restauranteRestMapper;

    @Operation(
            summary = "Crear restaurante",
            description = "Crea un restaurante nuevo. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/crear")
    @PreAuthorize(SecurityConstants.TIENE_ROL_ADMIN)
    public ResponseEntity<Void> crear(@Valid @RequestBody CrearRestauranteRequest request) {
        Restaurante restaurante = restauranteRestMapper.toDomain(request);
        crearRestauranteUseCase.ejecutar(restaurante);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Consultar id de restaurante por propietario",
            description = "Obtiene el id del restaurante asociado al propietario indicado.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/propietario/{idPropietario}")
    @PreAuthorize(SecurityConstants.TIENE_ROL_PROPIETARIO)
    public ResponseEntity<Long> consultarIdRestaurantePorPropietario(
            @PathVariable Integer idPropietario,
            Authentication authentication) {

        Integer idPropietarioAutenticado = Integer.parseInt(authentication.getName());
        if (!idPropietarioAutenticado.equals(idPropietario)) {
            throw new NoEsPropietarioException();
        }

        Long idRestaurante = consultarRestaurantePorPropietarioUseCase
                .obtenerIdRestaurantePorPropietario(idPropietario);
        return ResponseEntity.ok(idRestaurante);
    }
}
