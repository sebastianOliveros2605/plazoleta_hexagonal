package com.plazoleta.plazoleta_service.infrastructure.jpa.mapper;

import com.plazoleta.plazoleta_service.domain.model.DetallePedido;
import com.plazoleta.plazoleta_service.domain.model.Pedido;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.DetallePedidoEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PedidoEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(target = "restaurante", source = "idRestaurante", qualifiedByName = "toRestauranteEntity")
    @Mapping(target = "detallePedido", source = "detallePedido")
    PedidoEntity toEntity(Pedido pedido);

    @Mapping(target = "idRestaurante", source = "restaurante.id")
    @Mapping(target = "detallePedido", source = "detallePedido")
    Pedido toDomain(PedidoEntity entity);

    @Mapping(target = "plato.id", source = "idPlato")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    DetallePedidoEntity toDetailEntity(DetallePedido detalle);

    @Mapping(target = "idPlato", source = "plato.id")
    @Mapping(target = "idPedido", ignore = true)
    DetallePedido toDetailDomain(DetallePedidoEntity entity);

    @Named("toRestauranteEntity")
    default RestauranteEntity toRestauranteEntity(Long idRestaurante) {
        if (idRestaurante == null) {
            return null;
        }
        RestauranteEntity restauranteEntity = new RestauranteEntity();
        restauranteEntity.setId(idRestaurante);
        return restauranteEntity;
    }

    @AfterMapping
    default void linkPedidoToDetalles(Pedido source, @MappingTarget PedidoEntity target) {
        if (target.getDetallePedido() == null) {
            return;
        }
        for (DetallePedidoEntity detalle : target.getDetallePedido()) {
            detalle.setPedido(target);
        }
    }

    @AfterMapping
    default void setIdPedidoInDetalles(PedidoEntity entity, @MappingTarget Pedido pedido) {
        if (pedido.getDetallePedido() == null) {
            return;
        }
        for (DetallePedido detallePedido : pedido.getDetallePedido()) {
            detallePedido.setIdPedido(entity.getId());
        }
    }
}

