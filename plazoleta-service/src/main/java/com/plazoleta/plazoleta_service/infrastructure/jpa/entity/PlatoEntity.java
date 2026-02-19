package com.plazoleta.plazoleta_service.infrastructure.jpa.entity;

import com.plazoleta.plazoleta_service.domain.model.CategoriaPlatoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plato")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Integer precio;
    private String urlImagen;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private RestauranteEntity restaurante;
    @Enumerated(EnumType.STRING)
    private CategoriaPlatoEnum categoria;

    private Boolean activo;
}
