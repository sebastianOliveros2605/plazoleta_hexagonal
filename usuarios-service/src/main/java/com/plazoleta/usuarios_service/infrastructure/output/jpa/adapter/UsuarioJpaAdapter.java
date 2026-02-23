package com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter;

import java.util.Optional;

import com.plazoleta.usuarios_service.domain.exception.RolNoEncontradoException;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IUsuarioPersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.mapper.UsuarioMapper;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.RolJpaRepository;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.UsuarioJpaRepository;

public class UsuarioJpaAdapter implements IUsuarioPersistencePort {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final RolJpaRepository rolJpaRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioJpaAdapter(
            UsuarioJpaRepository usuarioJpaRepository,
            RolJpaRepository rolJpaRepository,
            UsuarioMapper usuarioMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.rolJpaRepository = rolJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Usuario save(Usuario usuario) {
        var rolEntity = rolJpaRepository.findById(usuario.getRol().getId())
                .orElseThrow(() -> new RolNoEncontradoException(usuario.getRol().getId()));
        return usuarioMapper.toDomain(
                usuarioJpaRepository.save(
                        usuarioMapper.toEntity(usuario, rolEntity)
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
