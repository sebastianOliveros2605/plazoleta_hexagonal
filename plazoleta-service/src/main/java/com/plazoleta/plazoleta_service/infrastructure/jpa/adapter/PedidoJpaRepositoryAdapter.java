package com.plazoleta.plazoleta_service.infrastructure.jpa.adapter;

import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.domain.ports.out.IPedidoRepositoryPort;
import com.plazoleta.plazoleta_service.infrastructure.jpa.mapper.PedidoMapper;
import com.plazoleta.plazoleta_service.infrastructure.jpa.repository.IPedidoJpaRepository;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PedidoEntity;
import org.hibernate.Hibernate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PedidoJpaRepositoryAdapter implements IPedidoRepositoryPort {

    private final IPedidoJpaRepository pedidoJpaRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public Boolean clienteConPedidosEnProceso(Integer idCliente) {
        return pedidoJpaRepository.existsByIdClienteAndEstadoIn(
                idCliente,
                List.of(EstadoPedidoEnum.PENDIENTE, EstadoPedidoEnum.EN_PREPARACION, EstadoPedidoEnum.LISTO)
        );
    }

    @Override
    @Transactional
    public Pedido consultarPedidoPorId(Long idPedido) {
        PedidoEntity entity = pedidoJpaRepository.findByIdWithDetalle(idPedido).orElse(null);
        if (entity == null) {
            return null;
        }
        Hibernate.initialize(entity.getDetallePedido());
        return pedidoMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public PaginacionResultado<Pedido> listarPorRestauranteYEstado(Long idRestaurante, EstadoPedidoEnum estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "fechaCreacion"));
        Page<Pedido> pedidosPage = pedidoJpaRepository
                .findByRestauranteIdAndEstado(idRestaurante, estado, pageable)
                .map(pedidoMapper::toDomain);

        return new PaginacionResultado<>(
                pedidosPage.getContent(),
                pedidosPage.getNumber(),
                pedidosPage.getSize(),
                pedidosPage.getTotalElements(),
                pedidosPage.getTotalPages(),
                pedidosPage.isLast());
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoMapper.toDomain(pedidoJpaRepository.save(pedidoMapper.toEntity(pedido)));
    }

    @Override
    public List<Long> consultarIdsFinalizadosPorRestaurante(Long idRestaurante) {
        return pedidoJpaRepository.findIdsByRestauranteIdAndEstadoIn(
                idRestaurante,
                List.of(EstadoPedidoEnum.ENTREGADO, EstadoPedidoEnum.CANCELADO));
    }

}
