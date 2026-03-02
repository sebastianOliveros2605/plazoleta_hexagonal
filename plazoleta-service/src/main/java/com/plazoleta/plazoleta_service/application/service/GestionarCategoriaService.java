package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.CategoriaNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Categoria;
import com.plazoleta.plazoleta_service.domain.ports.in.IGestionarCategoriaUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.ICategoriaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GestionarCategoriaService implements IGestionarCategoriaUseCase {
    private final ICategoriaRepositoryPort categoriaRepository;

    @Override
    public void guardar(Categoria categoria) {
        categoria.normalizarNombre();
        categoriaRepository.guardar(categoria);
    }

    @Override
    public Categoria buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.buscarPorId(id);
        if (categoria == null) {
            throw new CategoriaNoExisteException();
        }
        return categoria;
    }

    @Override
    public Categoria buscarPorNombre(String nombre) {
        Categoria categoria = categoriaRepository.buscarPorNombre(nombre);
        if (categoria == null) {
            throw new CategoriaNoExisteException();
        }
        return categoria;
    }
}
