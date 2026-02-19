package com.plazoleta.usuarios_service.infrastructure.input.rest.mapper;

import com.plazoleta.usuarios_service.domain.model.RolEnum;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.UsuarioDTO;

public class UsuarioDTOMapper {
    public static Usuario toUsuarioDomain(UsuarioDTO request,int rol) {

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setDocumentoIdentidad(request.getDocumentoIdentidad());
        usuario.setCelular(request.getCelular());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(request.getPassword());
        switch(rol){
            case 1:
                usuario.setRol(RolEnum.ADMIN);
                break;
            case 2:
                usuario.setRol(RolEnum.PROPIETARIO);
                break;
            case 3:
                usuario.setRol(RolEnum.EMPLEADO);
                break;
            case 4:
                usuario.setRol(RolEnum.CLIENTE);
                break;
        }
        return usuario;
    }

    public static UsuarioDTO toDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setCelular(usuario.getCelular());
        usuarioDTO.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioDTO.setRol(usuario.getRol().name());
        return usuarioDTO; 

    }
}
