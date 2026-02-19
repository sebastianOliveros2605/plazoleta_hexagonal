package com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter;

import java.util.Optional;

import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.mapper.UsuarioMapper;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.UsuarioJpaRepository;

public class UsuarioJpaAdapter implements IUsuarioPersistencePort {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioJpaAdapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return UsuarioMapper.toDomain(
                usuarioJpaRepository.save(
                        UsuarioMapper.toEntity(usuario)
                )
        );
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioJpaRepository
                .findByCorreo(correo)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return usuarioJpaRepository.existsByCorreo(correo);
    }

    @Override
    public Optional<Usuario> findById(Integer idUsuario) {
        return usuarioJpaRepository.findById(idUsuario)
            .map(UsuarioMapper::toDomain);
    }
}