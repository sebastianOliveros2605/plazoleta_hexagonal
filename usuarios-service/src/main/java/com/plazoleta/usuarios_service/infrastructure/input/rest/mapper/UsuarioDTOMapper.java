package com.plazoleta.usuarios_service.infrastructure.input.rest.mapper;

import com.plazoleta.usuarios_service.domain.model.Rol;
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
            @Mapping(target = "idRestaurante", ignore = true),
            @Mapping(target = "rol", source = "rol")
    })
    Usuario toUsuarioDomain(UsuarioDTO request, Rol rol);

    @Mapping(target = "rol", source = "rol.nombre")
    UsuarioDTO toDTO(Usuario usuario);
}
