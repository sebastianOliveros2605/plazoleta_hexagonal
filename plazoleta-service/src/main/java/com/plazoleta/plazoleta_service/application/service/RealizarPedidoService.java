package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.UsuarioConPedidosPendientes;
import com.plazoleta.plazoleta_service.domain.exception.PedidoInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.PlatoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.DetallePedido;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.domain.ports.in.IRealizarPedidoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.ITrazabilidadClientPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class RealizarPedidoService  implements IRealizarPedidoUseCase {
    private final IPedidoRepositoryPort pedidoRepositoryPort;
    private final IRestauranteRepositoryPort restauranteRepositoryPort;
    private final IPlatoRepositoryPort platoRepositoryPort;
    private final ITrazabilidadClientPort trazabilidadClientPort;
    private final IUsuarioClientPort usuarioClientPort;

    @Override
    public void realizarPedido(Pedido pedido) {
        if (pedido.getDetallePedido() == null || pedido.getDetallePedido().isEmpty()) {
            throw new PedidoInvalidoException("El pedido debe tener al menos un plato.");
        }

        if (restauranteRepositoryPort.buscarPorId(pedido.getIdRestaurante()).isEmpty()) {
            throw new RestauranteNoExisteException();
        }

        validarPlatosDelMismoRestaurante(pedido.getDetallePedido(), pedido.getIdRestaurante());

        if (pedidoRepositoryPort.clienteConPedidosEnProceso(pedido.getIdCliente())) {
            throw new UsuarioConPedidosPendientes();
        }

        pedido.setEstado(EstadoPedidoEnum.PENDIENTE);
        pedido.setFechaCreacion(new Date());
        Pedido pedidoGuardado = pedidoRepositoryPort.guardar(pedido);

        registrarTrazabilidadInicial(pedidoGuardado);
    }

    private void validarPlatosDelMismoRestaurante(List<DetallePedido> detallePedido, Long idRestaurante) {
        for (DetallePedido detalle : detallePedido) {
            var plato = platoRepositoryPort.buscarPorId(detalle.getIdPlato())
                    .orElseThrow(PlatoNoExisteException::new);
            if (!idRestaurante.equals(plato.getIdRestaurante())) {
                throw new PedidoInvalidoException("Todos los platos del pedido deben ser del mismo restaurante.");
            }
        }
    }

    private void registrarTrazabilidadInicial(Pedido pedidoGuardado) {
        try {
            trazabilidadClientPort.registrarCambioEstado(
                    pedidoGuardado.getId(),
                    pedidoGuardado.getIdRestaurante(),
                    pedidoGuardado.getIdCliente(),
                    obtenerCorreoCliente(pedidoGuardado.getIdCliente()),
                    null,
                    EstadoPedidoEnum.PENDIENTE,
                    null,
                    null
            );
        } catch (Exception exception) {
            log.error("No fue posible registrar trazabilidad inicial para pedido {}", pedidoGuardado.getId(), exception);
        }
    }

    private String obtenerCorreoCliente(Integer idCliente) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof String detalleCorreo) {
            return detalleCorreo;
        }
        return usuarioClientPort.consultarCorreoUsuario(idCliente);
    }
}
