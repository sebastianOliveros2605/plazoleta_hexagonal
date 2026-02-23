package com.plazoleta.usuarios_service.application.useCase;

import com.plazoleta.usuarios_service.domain.model.Rol;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.domain.puertosIn.IRestauranteClientePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CrearEmpleadoUseCase {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final IRestauranteClientePort restauranteClientePort;

    public Usuario crearEmpleado(Usuario usuario, Integer idPropietarioAutenticado) {
        usuario.setRol(new Rol(Rol.EMPLEADO_ID, Rol.EMPLEADO, null));
        Long idRestaurante = restauranteClientePort.consultarIdRestaurantePorPropietario(idPropietarioAutenticado);
        usuario.setIdRestaurante(idRestaurante);
        System.out.println("LOG EMPLEADO A CREAR:"+usuario.getRol().getNombre());
        return crearUsuarioUseCase.crearUsuario(usuario);
    }
}
