package com.plazoleta.plazoleta_service.domain.ports.out;

public interface IUsuarioClientPort {
    Long consultarIdUsuario();
    Long consultarIdRestauranteDeUsuario(Integer idUsuario);
    String consultarCorreoUsuario(Integer idUsuario);
    String consultarCelularUsuario(Integer idUsuario);
    String rolUsuarioString(Integer idUsuario);
    Boolean existeUsuario(Integer idUsuario);
}
