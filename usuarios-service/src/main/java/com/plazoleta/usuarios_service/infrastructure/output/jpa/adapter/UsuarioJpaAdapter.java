package com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter;

import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.mapper.UsuarioMapper;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.UsuarioJpaRepository;

import java.util.Optional;


public class UsuarioJpaAdapter implements IUsuarioPersistencePort {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioJpaAdapter(
            UsuarioJpaRepository usuarioJpaRepository,
            UsuarioMapper usuarioMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioMapper.toDomain(
                usuarioJpaRepository.save(
                        usuarioMapper.toEntity(usuario)
                )
        );
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioJpaRepository
                .findByCorreo(correo)
                .map(usuarioMapper::toDomain);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return usuarioJpaRepository.existsByCorreo(correo);
    }

    @Override
    public Optional<Usuario> findById(Integer idUsuario) {
        return usuarioJpaRepository.findById(idUsuario)
            .map(usuarioMapper::toDomain);
    }
}
