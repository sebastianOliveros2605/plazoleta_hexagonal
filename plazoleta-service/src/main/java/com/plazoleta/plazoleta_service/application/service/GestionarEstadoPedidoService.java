package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.constants.PedidoDomainConstants;
import com.plazoleta.plazoleta_service.domain.exception.PedidoNoAutorizadoException;
import com.plazoleta.plazoleta_service.domain.exception.PedidoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.TransicionEstadoNoPermitidaException;
import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.domain.ports.in.IGestionarEstadoPedidoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.INotificacionEventPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.ITrazabilidadClientPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class GestionarEstadoPedidoService implements IGestionarEstadoPedidoUseCase {

    private final IPedidoRepositoryPort pedidoRepositoryPort;
    private final IUsuarioClientPort usuarioClientPort;
    private final ITrazabilidadClientPort trazabilidadClientPort;
    private final INotificacionEventPort notificacionEventPort;

    @Override
    public void asignarPedidoEnPreparacion(Long idPedido, Integer idEmpleado, String correoEmpleado) {
        Pedido pedido = obtenerPedido(idPedido);
        validarEmpleadoDelRestaurante(idEmpleado, pedido);

        if (pedido.getEstado() != EstadoPedidoEnum.PENDIENTE) {
            throw new TransicionEstadoNoPermitidaException("Solo se puede asignar un pedido en estado PENDIENTE.");
        }

        pedido.setIdEmpleado(idEmpleado);
        cambiarEstadoYRegistrar(pedido, EstadoPedidoEnum.EN_PREPARACION, idEmpleado, correoEmpleado);
    }

    @Override
    public void marcarPedidoListo(Long idPedido, Integer idEmpleado, String correoEmpleado) {
        Pedido pedido = obtenerPedido(idPedido);
        validarEmpleadoDelPedido(idEmpleado, pedido);

        if (pedido.getEstado() != EstadoPedidoEnum.EN_PREPARACION) {
            throw new TransicionEstadoNoPermitidaException("Solo se puede marcar LISTO un pedido en estado EN_PREPARACION.");
        }

        cambiarEstadoYRegistrar(pedido, EstadoPedidoEnum.LISTO, idEmpleado, correoEmpleado);
    }

    @Override
    public void marcarPedidoEntregado(Long idPedido, Integer idEmpleado, String correoEmpleado, String pinSeguridad) {
        Pedido pedido = obtenerPedido(idPedido);
        validarEmpleadoDelPedido(idEmpleado, pedido);

        if (pedido.getEstado() != EstadoPedidoEnum.LISTO) {
            throw new TransicionEstadoNoPermitidaException("Solo se puede marcar ENTREGADO un pedido en estado LISTO.");
        }

        validarPinSeguridad(pinSeguridad, pedido.getPinSeguridad(), pedido.getPinExpiracion());
        cambiarEstadoYRegistrar(pedido, EstadoPedidoEnum.ENTREGADO, idEmpleado, correoEmpleado);
    }

    @Override
    public void cancelarPedido(Long idPedido, Integer idCliente, String correoCliente) {
        Pedido pedido = obtenerPedido(idPedido);

        if (!idCliente.equals(pedido.getIdCliente())) {
            throw new PedidoNoAutorizadoException("El cliente autenticado no puede cancelar este pedido.");
        }

        if (pedido.getEstado() != EstadoPedidoEnum.PENDIENTE) {
            throw new TransicionEstadoNoPermitidaException("Solo se puede cancelar un pedido en estado PENDIENTE.");
        }

        cambiarEstadoYRegistrar(pedido, EstadoPedidoEnum.CANCELADO, null, correoCliente);
    }

    private Pedido obtenerPedido(Long idPedido) {
        Pedido pedido = pedidoRepositoryPort.consultarPedidoPorId(idPedido);
        if (pedido == null) {
            throw new PedidoNoExisteException(idPedido);
        }
        return pedido;
    }

    private void validarEmpleadoDelRestaurante(Integer idEmpleado, Pedido pedido) {
        Long idRestauranteEmpleado = usuarioClientPort.consultarIdRestauranteDeUsuario(idEmpleado);
        if (idRestauranteEmpleado == null || !idRestauranteEmpleado.equals(pedido.getIdRestaurante())) {
            throw new PedidoNoAutorizadoException("El empleado no pertenece al restaurante del pedido.");
        }
    }

    private void validarEmpleadoDelPedido(Integer idEmpleado, Pedido pedido) {
        validarEmpleadoDelRestaurante(idEmpleado, pedido);

        if (pedido.getIdEmpleado() == null || !pedido.getIdEmpleado().equals(idEmpleado)) {
            throw new PedidoNoAutorizadoException("El pedido no esta asignado al empleado autenticado.");
        }
    }

    private void cambiarEstadoYRegistrar(
            Pedido pedido,
            EstadoPedidoEnum nuevoEstado,
            Integer idEmpleado,
            String correoActor) {

        EstadoPedidoEnum estadoAnterior = pedido.getEstado();
        pedido.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoPedidoEnum.LISTO) {
            pedido.setPinSeguridad(generarPinSeguridad());
            pedido.setPinExpiracion(calcularFechaExpiracionPin());
        }

        if (nuevoEstado == EstadoPedidoEnum.ENTREGADO || nuevoEstado == EstadoPedidoEnum.CANCELADO) {
            pedido.setFechaEntrega(new Date());
            pedido.setPinSeguridad(null);
            pedido.setPinExpiracion(null);
        }

        Pedido actualizado = pedidoRepositoryPort.guardar(pedido);
        registrarTrazabilidad(actualizado, estadoAnterior, nuevoEstado, idEmpleado, correoActor);

        if (nuevoEstado == EstadoPedidoEnum.LISTO) {
            notificarPedidoListo(actualizado, pedido.getPinSeguridad());
        }
    }

    private void registrarTrazabilidad(
            Pedido pedido,
            EstadoPedidoEnum estadoAnterior,
            EstadoPedidoEnum estadoNuevo,
            Integer idEmpleado,
            String correoActor) {

        try {
            String correoCliente = usuarioClientPort.consultarCorreoUsuario(pedido.getIdCliente());
            String correoEmpleado = idEmpleado != null ? correoActor : null;

            trazabilidadClientPort.registrarCambioEstado(
                    pedido.getId(),
                    pedido.getIdRestaurante(),
                    pedido.getIdCliente(),
                    correoCliente,
                    estadoAnterior,
                    estadoNuevo,
                    idEmpleado,
                    correoEmpleado
            );
        } catch (Exception exception) {
            log.error("No fue posible registrar trazabilidad para pedido {} en estado {}", pedido.getId(), estadoNuevo, exception);
        }
    }

    private void notificarPedidoListo(Pedido pedido, String pinSeguridad) {
        try {
            String celularCliente = usuarioClientPort.consultarCelularUsuario(pedido.getIdCliente());
            String mensaje = String.format(PedidoDomainConstants.MENSAJE_SMS_PEDIDO_LISTO, pinSeguridad);
            notificacionEventPort.notificarPedidoListo(
                    pedido.getId(),
                    pedido.getIdCliente(),
                    celularCliente,
                    mensaje,
                    pinSeguridad);
        } catch (Exception exception) {
            log.error("No fue posible publicar evento de notificacion para pedido {}", pedido.getId(), exception);
        }
    }

    private void validarPinSeguridad(String pinIngresado, String pinGuardado) {
        if (pinIngresado == null || pinIngresado.isBlank()) {
            throw new TransicionEstadoNoPermitidaException(PedidoDomainConstants.MENSAJE_PIN_OBLIGATORIO);
        }
        if (pinGuardado == null || !pinGuardado.equals(pinIngresado.trim())) {
            throw new TransicionEstadoNoPermitidaException(PedidoDomainConstants.MENSAJE_PIN_NO_COINCIDE);
        }
    }

    private void validarPinSeguridad(String pinIngresado, String pinGuardado, Date pinExpiracion) {
        if (pinExpiracion == null || pinExpiracion.before(new Date())) {
            throw new TransicionEstadoNoPermitidaException(PedidoDomainConstants.MENSAJE_PIN_EXPIRADO);
        }
        validarPinSeguridad(pinIngresado, pinGuardado);
    }

    private String generarPinSeguridad() {
        int longitud = PedidoDomainConstants.LONGITUD_PIN_SEGURIDAD;
        int min = (int) Math.pow(10, longitud - 1);
        int max = (int) Math.pow(10, longitud) - 1;
        int pin = ThreadLocalRandom.current().nextInt(min, max + 1);
        return String.valueOf(pin);
    }

    private Date calcularFechaExpiracionPin() {
        long ahora = System.currentTimeMillis();
        long expiracion = ahora + (PedidoDomainConstants.MINUTOS_EXPIRACION_PIN_SEGURIDAD * 60L * 1000L);
        return new Date(expiracion);
    }
}
