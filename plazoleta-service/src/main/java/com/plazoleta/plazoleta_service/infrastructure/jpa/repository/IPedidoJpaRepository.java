package com.plazoleta.plazoleta_service.infrastructure.jpa.repository;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IPedidoJpaRepository extends JpaRepository<PedidoEntity,Long> {
    boolean existsByIdClienteAndEstadoIn(Integer idCliente, Collection<EstadoPedidoEnum> estados);
    Page<PedidoEntity> findByRestauranteIdAndEstado(Long idRestaurante, EstadoPedidoEnum estado, Pageable pageable);

    @Query("""
            SELECT p
            FROM PedidoEntity p
            LEFT JOIN FETCH p.detallePedido d
            LEFT JOIN FETCH d.plato
            WHERE p.id = :idPedido
            """)
    Optional<PedidoEntity> findByIdWithDetalle(@Param("idPedido") Long idPedido);

    @Query("""
            SELECT p.id
            FROM PedidoEntity p
            WHERE p.restaurante.id = :idRestaurante
              AND p.estado IN :estados
            """)
    List<Long> findIdsByRestauranteIdAndEstadoIn(
            @Param("idRestaurante") Long idRestaurante,
            @Param("estados") Collection<EstadoPedidoEnum> estados);
}
