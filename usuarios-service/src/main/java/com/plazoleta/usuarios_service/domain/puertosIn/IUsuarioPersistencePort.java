package com.plazoleta.usuarios_service.domain.puertosIn;

import java.util.Optional;

import com.plazoleta.usuarios_service.domain.model.Usuario;

public interface IUsuarioPersistencePort {

    Usuario save(Usuario usuario);

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    Optional<Usuario>  findById(Integer idUsuario);
}
