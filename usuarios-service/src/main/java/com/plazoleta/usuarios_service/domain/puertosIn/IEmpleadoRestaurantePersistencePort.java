package com.plazoleta.usuarios_service.domain.puertosIn;

public interface IEmpleadoRestaurantePersistencePort {
    void saveOrUpdate(Integer idUsuario, Long idRestaurante);
}
