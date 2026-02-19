package com.plazoleta.plazoleta_service.infrastructure.jpa.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;

public interface RestauranteJpaRepository extends JpaRepository<RestauranteEntity, Long> {

    Optional<Restaurante> findByIdPropietario(Long idPropietario);
    
}
