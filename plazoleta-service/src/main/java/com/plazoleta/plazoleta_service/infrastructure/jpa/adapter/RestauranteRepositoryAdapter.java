package com.plazoleta.plazoleta_service.infrastructure.jpa.adapter;

import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.RestauranteEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.mapper.RestauranteMapper;
import com.plazoleta.plazoleta_service.infrastructure.jpa.repository.RestauranteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    @Override
    public PaginacionResultado<Restaurante> listar(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nombre"));
        Page<Restaurante> restaurantesPage = jpaRepository.findAll(pageable).map(restauranteMapper::toDomain);
        return new PaginacionResultado<>(
                restaurantesPage.getContent(),
                restaurantesPage.getNumber(),
                restaurantesPage.getSize(),
                restaurantesPage.getTotalElements(),
                restaurantesPage.getTotalPages(),
                restaurantesPage.isLast());
    }

}
