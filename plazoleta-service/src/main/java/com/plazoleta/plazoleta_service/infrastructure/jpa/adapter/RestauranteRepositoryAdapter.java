package com.plazoleta.plazoleta_service.infrastructure.jpa.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.mapper.RestauranteMapper;
import com.plazoleta.plazoleta_service.infrastructure.jpa.repository.RestauranteJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestauranteRepositoryAdapter implements IRestauranteRepositoryPort {
    private final RestauranteJpaRepository jpaRepository;

    @Override
    public Restaurante guardar(Restaurante restaurante) {
        RestauranteEntity entity = RestauranteMapper.toEntity(restaurante);
        jpaRepository.save(entity);
        return RestauranteMapper.toDomain(entity);
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long idRestaurante) {
        return jpaRepository.findById(idRestaurante)
                .map(RestauranteMapper::toDomain);
    }

}
