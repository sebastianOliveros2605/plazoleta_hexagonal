package com.plazoleta.trazabilidad_service.domain.ports.out;

import com.plazoleta.trazabilidad_service.domain.model.Trazabilidad;

import java.util.List;

public interface ITrazabilidadRepositoryPort {

    Trazabilidad guardar(Trazabilidad trazabilidad);

    List<Trazabilidad> consultarPorPedido(Long idPedido);

    List<Trazabilidad> consultarPorCliente(Integer idCliente);

    List<Trazabilidad> consultarPorRestaurante(Long idRestaurante);
}
