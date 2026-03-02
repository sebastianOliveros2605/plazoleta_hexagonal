package com.plazoleta.plazoleta_service.domain.ports.out;

import com.plazoleta.plazoleta_service.domain.model.Categoria;

public interface ICategoriaRepositoryPort {
    void guardar(Categoria categoria);
    Categoria buscarPorId(Long id);
    Categoria buscarPorNombre(String nombre);
}
