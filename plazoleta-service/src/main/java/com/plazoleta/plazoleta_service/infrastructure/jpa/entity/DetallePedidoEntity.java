package com.plazoleta.plazoleta_service.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detallePedido")
@Getter
@Setter
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;
    @ManyToOne
    @JoinColumn(name = "id_plato")
    private PlatoEntity plato;
    private Integer cantidad;
}
