package com.plazoleta.usuarios_service.application.useCase;

import com.plazoleta.usuarios_service.domain.exception.UsuarioNoEncontradoException;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsultarUsuarioUseCase {
    private final IUsuarioPersistencePort usuarioPersistencePort;

    public Usuario consultarPorId(Integer id) {
        return usuarioPersistencePort.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException());
    }

    public boolean existePorId(Integer id) {
        return usuarioPersistencePort.findById(id).isPresent();
    }
    
}
