package com.plazoleta.usuarios_service.infrastructure.output.jpa.mapper;

import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.UsuarioEntity;

public class UsuarioMapper {
    public static UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setApellido(usuario.getApellido());
        entity.setDocumentoIdentidad(usuario.getDocumentoIdentidad());
        entity.setCelular(usuario.getCelular());
        entity.setFechaNacimiento(usuario.getFechaNacimiento());
        entity.setCorreo(usuario.getCorreo());
        entity.setPassword(usuario.getPassword());
        entity.setRol(usuario.getRol());
        return entity;
    }

    public static Usuario toDomain(UsuarioEntity entity) {
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setNombre(entity.getNombre());
        usuario.setApellido(entity.getApellido());
        usuario.setDocumentoIdentidad(entity.getDocumentoIdentidad());
        usuario.setCelular(entity.getCelular());
        usuario.setFechaNacimiento(entity.getFechaNacimiento());
        usuario.setCorreo(entity.getCorreo());
        usuario.setPassword(entity.getPassword());
        usuario.setRol(entity.getRol());
        return usuario;
    }
}
