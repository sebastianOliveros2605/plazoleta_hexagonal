package com.plazoleta.usuarios_service.infrastructure.input.rest.mapper;

import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.UsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsuarioDTOMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "nombre", source = "request.nombre"),
            @Mapping(target = "apellido", source = "request.apellido"),
            @Mapping(target = "documentoIdentidad", source = "request.documentoIdentidad"),
            @Mapping(target = "celular", source = "request.celular"),
            @Mapping(target = "fechaNacimiento", source = "request.fechaNacimiento"),
            @Mapping(target = "correo", source = "request.correo"),
            @Mapping(target = "password", source = "request.password"),
            @Mapping(target = "rolId", ignore = true),
            @Mapping(target = "rol", ignore = true)
    })
    Usuario toUsuarioDomain(UsuarioDTO request);

    @Mapping(target = "rol", expression = "java(usuario.getRol() != null ? usuario.getRol().name() : null)")
    @Mapping(target = "idRestaurante", ignore = true)
    UsuarioDTO toDTO(Usuario usuario);
}
