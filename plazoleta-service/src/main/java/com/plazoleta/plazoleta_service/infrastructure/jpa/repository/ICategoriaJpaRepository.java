package com.plazoleta.plazoleta_service.infrastructure.jpa.repository;

import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoriaJpaRepository extends JpaRepository<CategoriaEntity,Long> {
    Optional<CategoriaEntity> findByNombre(String nombre);
}
