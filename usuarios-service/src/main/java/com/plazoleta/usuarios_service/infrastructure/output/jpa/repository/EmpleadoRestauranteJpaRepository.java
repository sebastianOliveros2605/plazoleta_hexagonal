package com.plazoleta.usuarios_service.infrastructure.output.jpa.repository;

import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.EmpleadoRestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRestauranteJpaRepository extends JpaRepository<EmpleadoRestauranteEntity, Integer> {
    Optional<EmpleadoRestauranteEntity> findByUsuario_Id(Integer idUsuario);
}
