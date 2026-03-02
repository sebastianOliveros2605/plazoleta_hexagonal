package com.plazoleta.trazabilidad_service.infrastructure.rest.controller;

import com.plazoleta.trazabilidad_service.domain.model.Trazabilidad;
import com.plazoleta.trazabilidad_service.domain.ports.in.IGestionarTrazabilidad;
import com.plazoleta.trazabilidad_service.infrastructure.rest.dto.request.TrazabilidadRequestDTO;
import com.plazoleta.trazabilidad_service.infrastructure.rest.dto.response.ReporteTiempoPedidoResponseDTO;
import com.plazoleta.trazabilidad_service.infrastructure.rest.dto.response.TrazabilidadResponseDTO;
import com.plazoleta.trazabilidad_service.infrastructure.rest.dto.response.TransicionTiempoResponseDTO;
import com.plazoleta.trazabilidad_service.infrastructure.rest.mapper.TrazabilidadRestMapper;
import com.plazoleta.trazabilidad_service.infrastructure.security.SecurityConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/trazabilidad")
@RequiredArgsConstructor
@Tag(name = "Trazabilidad", description = "Registro y consulta de historial de estados de pedidos")
public class TrazabilidadController {

    private final IGestionarTrazabilidad guardarTrazabilidadUseCase;
    private final TrazabilidadRestMapper trazabilidadRestMapper;

    @Operation(
            summary = "Registrar evento de trazabilidad",
            description = "Guarda una transicion de estado del pedido.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<TrazabilidadResponseDTO> registrar(
            @Valid @RequestBody TrazabilidadRequestDTO request,
            Authentication authentication) {
        completarCorreosDesdeToken(request, authentication);
        Trazabilidad guardado = guardarTrazabilidadUseCase.guardar(trazabilidadRestMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(trazabilidadRestMapper.toResponse(guardado));
    }

    @Operation(
            summary = "Consultar historial por pedido",
            description = "Retorna todos los eventos del pedido ordenados por fecha ascendente.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<TrazabilidadResponseDTO>> consultarPorPedido(@PathVariable Long idPedido) {
        List<TrazabilidadResponseDTO> response =
                trazabilidadRestMapper.toResponseList(guardarTrazabilidadUseCase.consultarPorPedido(idPedido));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Consultar historial por cliente",
            description = "Retorna eventos de trazabilidad de un cliente ordenados por fecha descendente.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<TrazabilidadResponseDTO>> consultarPorCliente(@PathVariable Integer idCliente) {
        List<TrazabilidadResponseDTO> response =
                trazabilidadRestMapper.toResponseList(guardarTrazabilidadUseCase.consultarPorCliente(idCliente));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Consultar historial por restaurante",
            description = "Retorna eventos de trazabilidad del restaurante ordenados por fecha ascendente.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_PROPIETARIO)
    @GetMapping("/restaurante/{idRestaurante}")
    public ResponseEntity<List<TrazabilidadResponseDTO>> consultarPorRestaurante(@PathVariable Long idRestaurante) {
        List<TrazabilidadResponseDTO> response =
                trazabilidadRestMapper.toResponseList(guardarTrazabilidadUseCase.consultarPorRestaurante(idRestaurante));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Reporte de tiempos por pedido",
            description = "Calcula duraciones entre transiciones de estado para un pedido.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/reporte/pedido/{idPedido}")
    public ResponseEntity<ReporteTiempoPedidoResponseDTO> reportePorPedido(@PathVariable Long idPedido) {
        List<Trazabilidad> historial = guardarTrazabilidadUseCase.consultarPorPedido(idPedido);
        if (historial.isEmpty()) {
            return ResponseEntity.ok(new ReporteTiempoPedidoResponseDTO(idPedido, 0, null, null, 0, List.of()));
        }

        List<TransicionTiempoResponseDTO> transiciones = new ArrayList<>();
        for (int i = 1; i < historial.size(); i++) {
            Trazabilidad anterior = historial.get(i - 1);
            Trazabilidad actual = historial.get(i);
            long duracionSegundos = Math.max(0L, (actual.getFecha().getTime() - anterior.getFecha().getTime()) / 1000);
            transiciones.add(new TransicionTiempoResponseDTO(
                    anterior.getEstadoNuevo(),
                    actual.getEstadoNuevo(),
                    duracionSegundos));
        }

        Trazabilidad primero = historial.get(0);
        Trazabilidad ultimo = historial.get(historial.size() - 1);
        long total = Math.max(0L, (ultimo.getFecha().getTime() - primero.getFecha().getTime()) / 1000);

        ReporteTiempoPedidoResponseDTO reporte = new ReporteTiempoPedidoResponseDTO(
                idPedido,
                historial.size(),
                primero.getFecha(),
                ultimo.getFecha(),
                total,
                transiciones);

        return ResponseEntity.ok(reporte);
    }

    private void completarCorreosDesdeToken(TrazabilidadRequestDTO request, Authentication authentication) {
        if (authentication == null || !(authentication.getDetails() instanceof String correo)) {
            return;
        }

        Integer idAutenticado;
        try {
            idAutenticado = Integer.parseInt(authentication.getName());
        } catch (NumberFormatException exception) {
            return;
        }

        if (request.getIdCliente() != null
                && request.getIdCliente().equals(idAutenticado)
                && request.getCorreoCliente() == null) {
            request.setCorreoCliente(correo);
        }

        if (request.getIdEmpleado() != null
                && request.getIdEmpleado().equals(idAutenticado)
                && request.getCorreoEmpleado() == null) {
            request.setCorreoEmpleado(correo);
        }
    }
}
