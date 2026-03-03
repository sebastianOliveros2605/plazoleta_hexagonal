package com.plazoleta.usuarios_service.domain.puertosIn;

import java.util.Optional;

public interface IRolPersistencePort {
    Optional<Integer> findIdByNombre(String nombreRol);
}
