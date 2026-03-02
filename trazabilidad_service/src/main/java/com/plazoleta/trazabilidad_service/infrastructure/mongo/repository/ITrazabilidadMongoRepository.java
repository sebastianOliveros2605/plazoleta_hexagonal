package com.plazoleta.trazabilidad_service.infrastructure.mongo.repository;

import com.plazoleta.trazabilidad_service.infrastructure.mongo.document.TrazabilidadDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITrazabilidadMongoRepository extends MongoRepository<TrazabilidadDocument, String> {

    List<TrazabilidadDocument> findByIdPedidoOrderByFechaAsc(Long idPedido);

    List<TrazabilidadDocument> findByIdClienteOrderByFechaDesc(Integer idCliente);

    List<TrazabilidadDocument> findByIdRestauranteOrderByFechaAsc(Long idRestaurante);
}
