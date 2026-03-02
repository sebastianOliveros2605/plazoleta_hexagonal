package com.plazoleta.plazoleta_service.infrastructure.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PlatoEntity;

public interface IPlatoJpaRepository extends JpaRepository<PlatoEntity, Long>{

    Page<PlatoEntity> findByRestauranteIdAndActivoTrue(Long idRestaurante, Pageable pageable);
    Page<PlatoEntity> findByRestauranteIdAndCategoriaIdAndActivoTrue(Long idRestaurante, Long idCategoria, Pageable pageable);
}
