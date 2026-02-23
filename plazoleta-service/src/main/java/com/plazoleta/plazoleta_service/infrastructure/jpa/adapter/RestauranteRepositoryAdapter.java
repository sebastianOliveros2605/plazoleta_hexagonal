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
    private final RestauranteMapper restauranteMapper;

    @Override
    public Restaurante guardar(Restaurante restaurante) {
        RestauranteEntity entity = restauranteMapper.toEntity(restaurante);
        jpaRepository.save(entity);
        return restauranteMapper.toDomain(entity);
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long idRestaurante) {
        return jpaRepository.findById(idRestaurante)
                .map(restauranteMapper::toDomain);
    }

    @Override
    public Optional<Restaurante> buscarPorIdPropietario(Integer idPropietario) {
        return jpaRepository.findByIdPropietario(idPropietario)
                .map(restauranteMapper::toDomain);
    }

}
