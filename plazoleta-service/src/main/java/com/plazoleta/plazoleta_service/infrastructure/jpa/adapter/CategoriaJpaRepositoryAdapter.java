package com.plazoleta.plazoleta_service.infrastructure.jpa.adapter;

import com.plazoleta.plazoleta_service.domain.model.Categoria;
import com.plazoleta.plazoleta_service.domain.ports.out.ICategoriaRepositoryPort;
import com.plazoleta.plazoleta_service.infrastructure.jpa.mapper.CategoriaMapper;
import com.plazoleta.plazoleta_service.infrastructure.jpa.repository.ICategoriaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoriaJpaRepositoryAdapter implements ICategoriaRepositoryPort {
    private final ICategoriaJpaRepository categoriaJpaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public void guardar(Categoria categoria) {
        categoriaJpaRepository.save(categoriaMapper.toEntity(categoria));
    }

    @Override
    public Categoria buscarPorId(Long id) {
        return categoriaJpaRepository.findById(id).map(categoriaMapper::toDomain).orElse(null);
    }

    @Override
    public Categoria buscarPorNombre(String nombre) {
        return categoriaJpaRepository.findByNombre(nombre).map(categoriaMapper::toDomain).orElse(null);
    }
}
