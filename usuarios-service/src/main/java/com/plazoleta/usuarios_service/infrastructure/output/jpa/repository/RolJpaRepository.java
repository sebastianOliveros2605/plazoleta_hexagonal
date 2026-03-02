package com.plazoleta.usuarios_service.infrastructure.output.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.RolEntity;

public interface RolJpaRepository extends JpaRepository<RolEntity, Integer> {
}

