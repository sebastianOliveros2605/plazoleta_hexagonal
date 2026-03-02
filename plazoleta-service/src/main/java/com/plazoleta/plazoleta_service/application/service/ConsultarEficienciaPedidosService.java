package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.PedidoInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoAsociadoRestauranteException;
import com.plazoleta.plazoleta_service.domain.model.EmpleadoEficiencia;
import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.FiltroEficienciaPedidos;
import com.plazoleta.plazoleta_service.domain.model.PedidoEficiencia;
import com.plazoleta.plazoleta_service.domain.model.ReporteEficienciaPedidos;
import com.plazoleta.plazoleta_service.domain.model.TrazabilidadEvento;
import com.plazoleta.plazoleta_service.domain.model.TransicionEficiencia;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarEficienciaPedidosUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.ITrazabilidadClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarEficienciaPedidosService implements IConsultarEficienciaPedidosUseCase {

    private static final EnumSet<EstadoPedidoEnum> ESTADOS_FINALIZADOS =
            EnumSet.of(EstadoPedidoEnum.ENTREGADO, EstadoPedidoEnum.CANCELADO);

    private final IRestauranteRepositoryPort restauranteRepositoryPort;
    private final IPedidoRepositoryPort pedidoRepositoryPort;
    private final ITrazabilidadClientPort trazabilidadClientPort;

    @Override
    public ReporteEficienciaPedidos consultar(Integer idPropietario, FiltroEficienciaPedidos filtro) {
        validarFiltro(idPropietario, filtro);

        Long idRestaurante = restauranteRepositoryPort.buscarPorIdPropietario(idPropietario)
                .map(restaurante -> restaurante.getId())
                .orElseThrow(UsuarioNoAsociadoRestauranteException::new);

        List<TrazabilidadEvento> eventos = new ArrayList<>(trazabilidadClientPort.consultarPorRestaurante(idRestaurante));
        completarEventosLegacySinRestaurante(idRestaurante, eventos);
        Map<Long, List<TrazabilidadEvento>> eventosPorPedido = eventos.stream()
                .collect(Collectors.groupingBy(TrazabilidadEvento::getIdPedido));

        List<PedidoEficiencia> pedidos = eventosPorPedido.values().stream()
                .map(this::construirPedidoEficiencia)
                .flatMap(Optional::stream)
                .filter(pedido -> filtrarPedido(pedido, filtro))
                .sorted(Comparator.comparing(PedidoEficiencia::getDuracionTotalSegundos).reversed())
                .collect(Collectors.toList());

        if (!filtro.isIncluirDetalleTransiciones()) {
            pedidos.forEach(pedido -> pedido.setTransiciones(List.of()));
        }

        List<EmpleadoEficiencia> ranking = construirRanking(pedidos, filtro.getIdEmpleado());

        ReporteEficienciaPedidos reporte = new ReporteEficienciaPedidos();
        reporte.setIdRestaurante(idRestaurante);
        reporte.setPedidos(pedidos);
        reporte.setRankingEmpleados(ranking);
        reporte.setTotalPedidosCompletados(pedidos.size());
        reporte.setTiempoPromedioSegundos((long) pedidos.stream()
                .mapToLong(PedidoEficiencia::getDuracionTotalSegundos)
                .average()
                .orElse(0));
        return reporte;
    }

    private void validarFiltro(Integer idPropietario, FiltroEficienciaPedidos filtro) {
        if (idPropietario == null || idPropietario <= 0) {
            throw new PedidoInvalidoException("El propietario autenticado es obligatorio.");
        }
        if (filtro == null) {
            throw new PedidoInvalidoException("El filtro de eficiencia es obligatorio.");
        }
        if (filtro.getIdPedido() != null && filtro.getIdPedido() <= 0) {
            throw new PedidoInvalidoException("El id del pedido debe ser positivo.");
        }
        if (filtro.getIdEmpleado() != null && filtro.getIdEmpleado() <= 0) {
            throw new PedidoInvalidoException("El id del empleado debe ser positivo.");
        }
        if (filtro.getFechaDesde() != null && filtro.getFechaHasta() != null
                && filtro.getFechaDesde().after(filtro.getFechaHasta())) {
            throw new PedidoInvalidoException("La fechaDesde no puede ser mayor que fechaHasta.");
        }
    }

    private Optional<PedidoEficiencia> construirPedidoEficiencia(List<TrazabilidadEvento> eventosPedido) {
        if (eventosPedido == null || eventosPedido.isEmpty()) {
            return Optional.empty();
        }

        List<TrazabilidadEvento> historial = eventosPedido.stream()
                .filter(evento -> evento.getFecha() != null)
                .sorted(Comparator.comparing(TrazabilidadEvento::getFecha))
                .toList();

        if (historial.isEmpty()) {
            return Optional.empty();
        }

        TrazabilidadEvento primero = historial.get(0);
        TrazabilidadEvento ultimo = historial.get(historial.size() - 1);
        if (!ESTADOS_FINALIZADOS.contains(ultimo.getEstadoNuevo())) {
            return Optional.empty();
        }

        PedidoEficiencia pedido = new PedidoEficiencia();
        pedido.setIdPedido(primero.getIdPedido());
        pedido.setFechaInicio(primero.getFecha());
        pedido.setFechaFin(ultimo.getFecha());
        pedido.setEstadoFinal(ultimo.getEstadoNuevo());
        pedido.setDuracionTotalSegundos(calcularSegundos(primero.getFecha(), ultimo.getFecha()));
        pedido.setTotalEventos(historial.size());
        pedido.setIdEmpleado(obtenerEmpleadoAsignado(historial));
        pedido.setTransiciones(construirTransiciones(historial));
        return Optional.of(pedido);
    }

    private Integer obtenerEmpleadoAsignado(List<TrazabilidadEvento> historial) {
        for (int i = historial.size() - 1; i >= 0; i--) {
            Integer idEmpleado = historial.get(i).getIdEmpleado();
            if (idEmpleado != null) {
                return idEmpleado;
            }
        }
        return null;
    }

    private List<TransicionEficiencia> construirTransiciones(List<TrazabilidadEvento> historial) {
        List<TransicionEficiencia> transiciones = new ArrayList<>();
        for (int i = 1; i < historial.size(); i++) {
            TrazabilidadEvento anterior = historial.get(i - 1);
            TrazabilidadEvento actual = historial.get(i);
            TransicionEficiencia transicion = new TransicionEficiencia();
            transicion.setEstadoDesde(anterior.getEstadoNuevo());
            transicion.setEstadoHasta(actual.getEstadoNuevo());
            transicion.setDuracionSegundos(calcularSegundos(anterior.getFecha(), actual.getFecha()));
            transiciones.add(transicion);
        }
        return transiciones;
    }

    private long calcularSegundos(Date fechaInicio, Date fechaFin) {
        return Math.max(0L, (fechaFin.getTime() - fechaInicio.getTime()) / 1000L);
    }

    private boolean filtrarPedido(PedidoEficiencia pedido, FiltroEficienciaPedidos filtro) {
        if (filtro.getIdPedido() != null && !filtro.getIdPedido().equals(pedido.getIdPedido())) {
            return false;
        }
        if (filtro.getIdEmpleado() != null && !Objects.equals(filtro.getIdEmpleado(), pedido.getIdEmpleado())) {
            return false;
        }
        if (filtro.getFechaDesde() != null && pedido.getFechaFin().before(filtro.getFechaDesde())) {
            return false;
        }
        if (filtro.getFechaHasta() != null && pedido.getFechaFin().after(filtro.getFechaHasta())) {
            return false;
        }
        return true;
    }

    private List<EmpleadoEficiencia> construirRanking(List<PedidoEficiencia> pedidos, Integer idEmpleadoFiltro) {
        Map<Integer, List<PedidoEficiencia>> pedidosPorEmpleado = pedidos.stream()
                .filter(pedido -> pedido.getIdEmpleado() != null)
                .collect(Collectors.groupingBy(PedidoEficiencia::getIdEmpleado));

        return pedidosPorEmpleado.entrySet().stream()
                .filter(entry -> idEmpleadoFiltro == null || idEmpleadoFiltro.equals(entry.getKey()))
                .map(entry -> {
                    List<PedidoEficiencia> pedidosEmpleado = entry.getValue();
                    EmpleadoEficiencia eficiencia = new EmpleadoEficiencia();
                    eficiencia.setIdEmpleado(entry.getKey());
                    eficiencia.setTotalPedidosCompletados(pedidosEmpleado.size());
                    eficiencia.setTiempoPromedioSegundos((long) pedidosEmpleado.stream()
                            .mapToLong(PedidoEficiencia::getDuracionTotalSegundos)
                            .average()
                            .orElse(0));
                    eficiencia.setTiempoMinimoSegundos(pedidosEmpleado.stream()
                            .mapToLong(PedidoEficiencia::getDuracionTotalSegundos)
                            .min()
                            .orElse(0));
                    eficiencia.setTiempoMaximoSegundos(pedidosEmpleado.stream()
                            .mapToLong(PedidoEficiencia::getDuracionTotalSegundos)
                            .max()
                            .orElse(0));
                    return eficiencia;
                })
                .sorted(Comparator.comparing(EmpleadoEficiencia::getTiempoPromedioSegundos))
                .toList();
    }

    private void completarEventosLegacySinRestaurante(Long idRestaurante, List<TrazabilidadEvento> eventosActuales) {
        List<Long> idsFinalizados = pedidoRepositoryPort.consultarIdsFinalizadosPorRestaurante(idRestaurante);
        if (idsFinalizados.isEmpty()) {
            return;
        }

        Map<Long, List<TrazabilidadEvento>> eventosPorPedido = eventosActuales.stream()
                .collect(Collectors.groupingBy(TrazabilidadEvento::getIdPedido));

        for (Long idPedido : idsFinalizados) {
            if (eventosPorPedido.containsKey(idPedido)) {
                continue;
            }
            List<TrazabilidadEvento> historialPedido = trazabilidadClientPort.consultarPorPedido(idPedido);
            eventosActuales.addAll(historialPedido);
        }
    }
}
