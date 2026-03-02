package com.plazoleta.plazoleta_service.infrastructure.jpa.entity;


import com.plazoleta.plazoleta_service.domain.model.EstadoPedidoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="pedido")
@Getter
@Setter
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer idCliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private RestauranteEntity restaurante;
    @Enumerated(EnumType.STRING)
    private EstadoPedidoEnum estado;
    private Date fechaCreacion;
    private Integer idEmpleado;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedidoEntity> detallePedido;
    private Date fechaEntrega;
    @Column(name = "pin_seguridad")
    private String pinSeguridad;
}
