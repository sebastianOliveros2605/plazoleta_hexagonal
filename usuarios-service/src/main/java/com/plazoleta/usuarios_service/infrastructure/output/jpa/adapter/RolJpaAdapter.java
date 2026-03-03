package com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter;

import com.plazoleta.usuarios_service.domain.puertosIn.IRolPersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.RolJpaRepository;

import java.util.Optional;

public class RolJpaAdapter implements IRolPersistencePort {

    private final RolJpaRepository rolJpaRepository;

    public RolJpaAdapter(RolJpaRepository rolJpaRepository) {
        this.rolJpaRepository = rolJpaRepository;
    }

    @Override
    public Optional<Integer> findIdByNombre(String nombreRol) {
        return rolJpaRepository.findByNombre(nombreRol).map(rol -> rol.getId());
    }
}
