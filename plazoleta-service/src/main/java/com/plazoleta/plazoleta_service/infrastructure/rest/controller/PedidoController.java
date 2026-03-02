package com.plazoleta.plazoleta_service.infrastructure.rest.controller;


import com.plazoleta.plazoleta_service.domain.ports.in.IRealizarPedidoUseCase;
import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.FiltroEficienciaPedidos;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarPedidoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarEficienciaPedidosUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.IGestionarEstadoPedidoUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.EntregarPedidoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PedidoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.ReporteEficienciaResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PageResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PedidoResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.PedidoRestMapper;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.ReporteEficienciaRestMapper;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.constraints.Min;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Operaciones para gestion de pedidos")
@Validated
public class PedidoController {

    private final IRealizarPedidoUseCase realizarPedidoUseCase;
    private final IConsultarPedidoUseCase consultarPedidoUseCase;
    private final IConsultarEficienciaPedidosUseCase consultarEficienciaPedidosUseCase;
    private final IGestionarEstadoPedidoUseCase gestionarEstadoPedidoUseCase;
    private final PedidoRestMapper pedidoMapper;
    private final ReporteEficienciaRestMapper reporteEficienciaRestMapper;

    @Operation(
            summary = "Realizar pedido",
            description = "Inicia el flujo de la generacion de un pedido en un restaurante especifico.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_CLIENTE)
    @PostMapping("/realizarPedido")
    public ResponseEntity<Void> realizarPedido(
            @Valid @RequestBody PedidoRequestDTO request,
            Authentication authentication) {
        Integer clienteId = Integer.parseInt(authentication.getName());
        request.setIdCliente(clienteId);
        realizarPedidoUseCase.realizarPedido(pedidoMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Listar pedidos por estado",
            description = "Lista los pedidos del restaurante del empleado autenticado, filtrados por estado y paginados.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_EMPLEADO)
    @GetMapping("/listar")
    public ResponseEntity<PageResponseDTO<PedidoResponseDTO>> listarPedidosPorEstado(
            @RequestParam EstadoPedidoEnum estado,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            Authentication authentication) {

        Integer idEmpleado = Integer.parseInt(authentication.getName());
        PaginacionResultado<Pedido> pageResult = consultarPedidoUseCase
                .listarPedidosPorEstado(idEmpleado, estado, page, size);
        List<PedidoResponseDTO> content = pedidoMapper.toResponseList(pageResult.getContent());

        PageResponseDTO<PedidoResponseDTO> response = new PageResponseDTO<>(
                content,
                pageResult.getPage(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Asignar pedido a empleado",
            description = "Asigna el pedido al empleado autenticado y cambia el estado a EN_PREPARACION.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_EMPLEADO)
    @PatchMapping("/{idPedido}/asignar")
    public ResponseEntity<Void> asignarPedido(@PathVariable Long idPedido, Authentication authentication) {
        Integer idEmpleado = Integer.parseInt(authentication.getName());
        gestionarEstadoPedidoUseCase.asignarPedidoEnPreparacion(idPedido, idEmpleado, extraerCorreo(authentication));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Marcar pedido listo",
            description = "Permite al empleado asignado cambiar el estado del pedido a LISTO.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_EMPLEADO)
    @PatchMapping("/{idPedido}/listo")
    public ResponseEntity<Void> marcarListo(@PathVariable Long idPedido, Authentication authentication) {
        Integer idEmpleado = Integer.parseInt(authentication.getName());
        gestionarEstadoPedidoUseCase.marcarPedidoListo(idPedido, idEmpleado, extraerCorreo(authentication));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Marcar pedido entregado",
            description = "Permite al empleado asignado cambiar el estado del pedido a ENTREGADO.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_EMPLEADO)
    @PatchMapping("/{idPedido}/entregar")
    public ResponseEntity<Void> marcarEntregado(
            @PathVariable Long idPedido,
            @Valid @RequestBody EntregarPedidoRequestDTO request,
            Authentication authentication) {
        Integer idEmpleado = Integer.parseInt(authentication.getName());
        gestionarEstadoPedidoUseCase.marcarPedidoEntregado(
                idPedido,
                idEmpleado,
                extraerCorreo(authentication),
                request.getPinSeguridad());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Cancelar pedido",
            description = "Permite al cliente cancelar su pedido solo si esta en estado PENDIENTE.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_CLIENTE)
    @PatchMapping("/{idPedido}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long idPedido, Authentication authentication) {
        Integer idCliente = Integer.parseInt(authentication.getName());
        gestionarEstadoPedidoUseCase.cancelarPedido(idPedido, idCliente, extraerCorreo(authentication));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Consultar eficiencia de pedidos del restaurante",
            description = "Reporte para propietarios con tiempos por pedido y ranking por empleado. "
                    + "Admite filtros opcionales por pedido, empleado y rango de fechas.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_PROPIETARIO)
    @GetMapping("/reporte/eficiencia")
    public ResponseEntity<ReporteEficienciaResponseDTO> consultarEficienciaPedidos(
            @RequestParam(required = false) Long idPedido,
            @RequestParam(required = false) Integer idEmpleado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaHasta,
            @RequestParam(defaultValue = "true") boolean incluirDetalleTransiciones,
            Authentication authentication) {
        Integer idPropietario = Integer.parseInt(authentication.getName());

        FiltroEficienciaPedidos filtro = new FiltroEficienciaPedidos();
        filtro.setIdPedido(idPedido);
        filtro.setIdEmpleado(idEmpleado);
        filtro.setFechaDesde(fechaDesde);
        filtro.setFechaHasta(fechaHasta);
        filtro.setIncluirDetalleTransiciones(incluirDetalleTransiciones);

        var reporte = consultarEficienciaPedidosUseCase.consultar(idPropietario, filtro);
        return ResponseEntity.ok(reporteEficienciaRestMapper.toResponse(reporte));
    }

    private String extraerCorreo(Authentication authentication) {
        Object details = authentication.getDetails();
        return details instanceof String ? (String) details : null;
    }
}
