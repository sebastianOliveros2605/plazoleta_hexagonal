package com.plazoleta.plazoleta_service.domain.ports.in;

import com.plazoleta.plazoleta_service.domain.model.Categoria;

public interface IGestionarCategoriaUseCase {
    void guardar(Categoria categoria);
    Categoria buscarPorId(Long id);
    Categoria buscarPorNombre(String nombre);
}
