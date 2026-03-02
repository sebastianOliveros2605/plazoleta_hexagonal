package com.plazoleta.trazabilidad_service.application.services;

import com.plazoleta.trazabilidad_service.domain.exception.TrazabilidadInvalidaException;
import com.plazoleta.trazabilidad_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.trazabilidad_service.domain.model.Trazabilidad;
import com.plazoleta.trazabilidad_service.domain.ports.in.IGestionarTrazabilidad;
import com.plazoleta.trazabilidad_service.domain.ports.out.ITrazabilidadRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GestionarTrazabilidadService implements IGestionarTrazabilidad {

    private final ITrazabilidadRepositoryPort trazabilidadRepositoryPort;

    @Override
    public Trazabilidad guardar(Trazabilidad trazabilidad) {
        validar(trazabilidad);

        if (trazabilidad.getFecha() == null) {
            trazabilidad.setFecha(new Date());
        }

        return trazabilidadRepositoryPort.guardar(trazabilidad);
    }

    @Override
    public List<Trazabilidad> consultarPorPedido(Long idPedido) {
        if (idPedido == null || idPedido <= 0) {
            throw new TrazabilidadInvalidaException("El id del pedido es obligatorio y debe ser positivo.");
        }
        return trazabilidadRepositoryPort.consultarPorPedido(idPedido);
    }

    @Override
    public List<Trazabilidad> consultarPorCliente(Integer idCliente) {
        if (idCliente == null || idCliente <= 0) {
            throw new TrazabilidadInvalidaException("El id del cliente es obligatorio y debe ser positivo.");
        }
        return trazabilidadRepositoryPort.consultarPorCliente(idCliente);
    }

    @Override
    public List<Trazabilidad> consultarPorRestaurante(Long idRestaurante) {
        if (idRestaurante == null || idRestaurante <= 0) {
            throw new TrazabilidadInvalidaException("El id del restaurante es obligatorio y debe ser positivo.");
        }
        return trazabilidadRepositoryPort.consultarPorRestaurante(idRestaurante);
    }

    private void validar(Trazabilidad trazabilidad) {
        if (trazabilidad == null) {
            throw new TrazabilidadInvalidaException("La trazabilidad es obligatoria.");
        }
        if (trazabilidad.getIdPedido() == null || trazabilidad.getIdPedido() <= 0) {
            throw new TrazabilidadInvalidaException("El id del pedido es obligatorio y debe ser positivo.");
        }
        if (trazabilidad.getIdRestaurante() == null || trazabilidad.getIdRestaurante() <= 0) {
            throw new TrazabilidadInvalidaException("El id del restaurante es obligatorio y debe ser positivo.");
        }
        if (trazabilidad.getEstadoNuevo() == null) {
            throw new TrazabilidadInvalidaException("El estado nuevo es obligatorio.");
        }
        if (trazabilidad.getEstadoAnterior() != null && trazabilidad.getEstadoAnterior() == trazabilidad.getEstadoNuevo()) {
            throw new TrazabilidadInvalidaException("El estado anterior no puede ser igual al estado nuevo.");
        }
        if (trazabilidad.getEstadoNuevo() == EstadoPedidoEnum.PENDIENTE && trazabilidad.getEstadoAnterior() != null) {
            throw new TrazabilidadInvalidaException("PENDIENTE solo puede usarse como primer estado del pedido.");
        }
    }
}
