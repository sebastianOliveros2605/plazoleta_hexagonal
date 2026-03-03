package com.plazoleta.usuarios_service.infrastructure.output.jpa.mapper;

import com.plazoleta.usuarios_service.domain.model.RolNombre;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.RolEntity;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "nombre", source = "nombre"),
            @Mapping(target = "apellido", source = "apellido"),
            @Mapping(target = "documentoIdentidad", source = "documentoIdentidad"),
            @Mapping(target = "celular", source = "celular"),
            @Mapping(target = "fechaNacimiento", source = "fechaNacimiento"),
            @Mapping(target = "correo", source = "correo"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "rol", expression = "java(toRolEntity(usuario.getRolId()))")
    })
    UsuarioEntity toEntity(Usuario usuario);

    @Mappings({
            @Mapping(target = "rol", expression = "java(toRolNombre(entity.getRol()))"),
            @Mapping(target = "rolId", expression = "java(entity.getRol() != null ? entity.getRol().getId() : null)")
    })
    Usuario toDomain(UsuarioEntity entity);

    default RolEntity toRolEntity(Integer rolId) {
        if (rolId == null) {
            return null;
        }
        RolEntity rol = new RolEntity();
        rol.setId(rolId);
        return rol;
    }

    default RolNombre toRolNombre(RolEntity rolEntity) {
        if (rolEntity == null || rolEntity.getNombre() == null) {
            return null;
        }
        return RolNombre.valueOf(rolEntity.getNombre());
    }
}
