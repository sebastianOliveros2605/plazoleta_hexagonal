package com.plazoleta.plazoleta_service.domain.ports.in;

public interface ICambiarEstadoActivoPlato {
    void habilitarDeshabilitarPlato(Long idPlato, Integer idPropietario, Boolean habilitar);
}
