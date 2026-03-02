package com.plazoleta.trazabilidad_service.infrastructure.mongo.adapter;

import com.plazoleta.trazabilidad_service.domain.model.Trazabilidad;
import com.plazoleta.trazabilidad_service.domain.ports.out.ITrazabilidadRepositoryPort;
import com.plazoleta.trazabilidad_service.infrastructure.mongo.mapper.TrazabilidadMongoMapper;
import com.plazoleta.trazabilidad_service.infrastructure.mongo.repository.ITrazabilidadMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrazabilidadMongoAdapter implements ITrazabilidadRepositoryPort {

    private final ITrazabilidadMongoRepository trazabilidadMongoRepository;
    private final TrazabilidadMongoMapper trazabilidadMongoMapper;

    @Override
    public Trazabilidad guardar(Trazabilidad trazabilidad) {
        var saved = trazabilidadMongoRepository.save(trazabilidadMongoMapper.toDocument(trazabilidad));
        return trazabilidadMongoMapper.toModel(saved);
    }

    @Override
    public List<Trazabilidad> consultarPorPedido(Long idPedido) {
        return trazabilidadMongoMapper.toModelList(
                trazabilidadMongoRepository.findByIdPedidoOrderByFechaAsc(idPedido));
    }

    @Override
    public List<Trazabilidad> consultarPorCliente(Integer idCliente) {
        return trazabilidadMongoMapper.toModelList(
                trazabilidadMongoRepository.findByIdClienteOrderByFechaDesc(idCliente));
    }

    @Override
    public List<Trazabilidad> consultarPorRestaurante(Long idRestaurante) {
        return trazabilidadMongoMapper.toModelList(
                trazabilidadMongoRepository.findByIdRestauranteOrderByFechaAsc(idRestaurante));
    }
}
