package com.plazoleta.plazoleta_service.infrastructure.jpa.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PlatoEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.mapper.PlatoMapper;
import com.plazoleta.plazoleta_service.infrastructure.jpa.repository.IPlatoJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlatoJpaRepositoryAdapter implements IPlatoRepositoryPort {

    private final IPlatoJpaRepository jpaRepository;

    @Override
    public Plato guardar(Plato plato) {
        PlatoEntity entity = PlatoMapper.toEntity(plato);
        PlatoEntity saved = jpaRepository.save(entity);
        return PlatoMapper.toDomain(saved);
    }

    @Override
    public Optional<Plato> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(PlatoMapper::toDomain);
    }

}
