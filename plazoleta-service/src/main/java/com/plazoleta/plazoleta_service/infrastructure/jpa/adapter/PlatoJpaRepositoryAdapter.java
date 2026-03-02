package com.plazoleta.plazoleta_service.infrastructure.jpa.adapter;

import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.infrastructure.jpa.entity.PlatoEntity;
import com.plazoleta.plazoleta_service.infrastructure.jpa.mapper.PlatoMapper;
import com.plazoleta.plazoleta_service.infrastructure.jpa.repository.IPlatoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlatoJpaRepositoryAdapter implements IPlatoRepositoryPort {

    private final IPlatoJpaRepository jpaRepository;
    private final PlatoMapper platoMapper;

    @Override
    public Plato guardar(Plato plato) {
        PlatoEntity entity = platoMapper.toEntity(plato);
        PlatoEntity saved = jpaRepository.save(entity);
        return platoMapper.toDomain(saved);
    }

    @Override
    public Optional<Plato> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(platoMapper::toDomain);
    }

    @Override
    public PaginacionResultado<Plato> listarPorRestaurante(Long idRestaurante, Long idCategoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nombre"));

        Page<Plato> platosPage = (idCategoria == null)
                ? jpaRepository.findByRestauranteIdAndActivoTrue(idRestaurante, pageable).map(platoMapper::toDomain)
                : jpaRepository.findByRestauranteIdAndCategoriaIdAndActivoTrue(idRestaurante, idCategoria, pageable)
                        .map(platoMapper::toDomain);

        return new PaginacionResultado<>(
                platosPage.getContent(),
                platosPage.getNumber(),
                platosPage.getSize(),
                platosPage.getTotalElements(),
                platosPage.getTotalPages(),
                platosPage.isLast());
    }

}
