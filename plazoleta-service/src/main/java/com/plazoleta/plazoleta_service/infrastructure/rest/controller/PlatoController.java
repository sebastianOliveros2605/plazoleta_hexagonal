package com.plazoleta.plazoleta_service.infrastructure.rest.controller;

import com.plazoleta.plazoleta_service.domain.constants.RoleConstants;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.ports.in.ICambiarEstadoActivoPlato;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.IModificarPlatoUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.ModificarPlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PageResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PlatoResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.PlatoRestMapper;
import com.plazoleta.plazoleta_service.infrastructure.security.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/platos")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "Operaciones para gestion de platos")
public class PlatoController {

    private final ICrearPlatoUseCase crearPlatoUseCase;
    private final IModificarPlatoUseCase modificarPlatoUseCase;
    private final ICambiarEstadoActivoPlato cambiarEstadoActivoPlato;
    private final IConsultarPlatoUseCase consultarPlatoUseCase;
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

    @Operation(
            summary = "Habilitar/Deshabilitar plato",
            description = "Actualiza el estado del plato en un restaurante (Activo/Inactivo).",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_PROPIETARIO)
    @PutMapping("/cambiarEstadoPlato/{idPLato}/{habilitar}")
    public ResponseEntity<Void> cambiarEstadoPlato(
            @PathVariable("idPLato") Long idPlato,
            @PathVariable Boolean habilitar,
            Authentication authentication) {

        Integer propietarioId = Integer.parseInt(authentication.getName());
        cambiarEstadoActivoPlato.habilitarDeshabilitarPlato(idPlato,propietarioId,habilitar);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Listar platos por restaurante",
            description = "Lista los platos de un restaurante filtrados por categoria o no, el resultado esta paginado.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('"+ RoleConstants.ROL_CLIENTE+"','"+RoleConstants.ROL_PROPIETARIO+"','"+RoleConstants.ROL_EMPLEADO+"')")
    public ResponseEntity<PageResponseDTO<PlatoResponseDTO>> listarPlatosPorRestaurante(
            @RequestParam Long idRestaurante,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {

        PaginacionResultado<Plato> pageResult = consultarPlatoUseCase
                .listarPlatosPorRestaurante(idRestaurante, idCategoria, page, size);
        List<PlatoResponseDTO> content = platoRestMapper.toResponseList(pageResult.getContent());

        PageResponseDTO<PlatoResponseDTO> response = new PageResponseDTO<>(
                content,
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast());

        return ResponseEntity.ok(response);
    }
}
