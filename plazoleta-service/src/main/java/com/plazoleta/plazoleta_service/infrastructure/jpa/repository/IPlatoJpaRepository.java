package com.plazoleta.plazoleta_service.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PlatoEntity;

public interface IPlatoJpaRepository extends JpaRepository<PlatoEntity, Long>{
    
}
