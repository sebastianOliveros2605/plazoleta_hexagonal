package com.plazoleta.plazoleta_service.domain.ports.out;

public interface IUsuarioClientPort {
    Long consultarIdUsuario();
    String rolUsuarioString(Integer idUsuario);
    Boolean existeUsuario(Integer idUsuario);
}
