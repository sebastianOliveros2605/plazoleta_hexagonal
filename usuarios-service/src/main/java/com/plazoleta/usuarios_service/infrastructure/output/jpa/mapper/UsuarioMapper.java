package com.plazoleta.usuarios_service.infrastructure.output.jpa.mapper;

import com.plazoleta.usuarios_service.domain.model.Rol;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.RolEntity;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mappings({
            @Mapping(target = "id", source = "usuario.id"),
            @Mapping(target = "nombre", source = "usuario.nombre"),
            @Mapping(target = "apellido", source = "usuario.apellido"),
            @Mapping(target = "documentoIdentidad", source = "usuario.documentoIdentidad"),
            @Mapping(target = "celular", source = "usuario.celular"),
            @Mapping(target = "fechaNacimiento", source = "usuario.fechaNacimiento"),
            @Mapping(target = "correo", source = "usuario.correo"),
            @Mapping(target = "password", source = "usuario.password"),
            @Mapping(target = "idRestaurante", source = "usuario.idRestaurante"),
            @Mapping(target = "rol", source = "rolEntity")
    })
    UsuarioEntity toEntity(Usuario usuario, RolEntity rolEntity);

    Usuario toDomain(UsuarioEntity entity);

    Rol toRol(RolEntity entity);
}
