package com.plazoleta.plazoleta_service.infrastructure.rest.controller;

import com.plazoleta.plazoleta_service.domain.constants.RoleConstants;
import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarRestauranteUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearRestauranteUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.CrearRestauranteRequest;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PageResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.RestauranteResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.RestauranteRestMapper;
import com.plazoleta.plazoleta_service.infrastructure.security.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Operaciones para gestion de restaurantes")
@Validated
public class RestauranteController {

    private final ICrearRestauranteUseCase crearRestauranteUseCase;
    private final IConsultarRestauranteUseCase consultarRestauranteUseCase;
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

        Long idRestaurante = consultarRestauranteUseCase
                .obtenerIdRestaurantePorPropietario(idPropietario);
        return ResponseEntity.ok(idRestaurante);
    }

    @Operation(
            summary = "Listar restaurantes",
            description = "Lista restaurantes por orden alfabetico y paginados.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('"+ RoleConstants.ROL_CLIENTE+"','"+RoleConstants.ROL_ADMIN+"')")
    public ResponseEntity<PageResponseDTO<RestauranteResponseDTO>> listarRestaurantes(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {

        PaginacionResultado<Restaurante> pageResult = consultarRestauranteUseCase.listarRestaurantes(page, size);
        List<RestauranteResponseDTO> content = restauranteRestMapper.toResponseList(pageResult.getContent());

        PageResponseDTO<RestauranteResponseDTO> response = new PageResponseDTO<>(
                content,
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast());

        return ResponseEntity.ok(response);
    }
}
