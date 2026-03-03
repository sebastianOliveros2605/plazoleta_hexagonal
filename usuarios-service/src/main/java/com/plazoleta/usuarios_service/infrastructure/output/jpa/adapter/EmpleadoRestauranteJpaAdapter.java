package com.plazoleta.usuarios_service.infrastructure.output.jpa.adapter;

import com.plazoleta.usuarios_service.domain.puertosIn.IEmpleadoRestaurantePersistencePort;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.EmpleadoRestauranteEntity;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.entity.UsuarioEntity;
import com.plazoleta.usuarios_service.infrastructure.output.jpa.repository.EmpleadoRestauranteJpaRepository;

public class EmpleadoRestauranteJpaAdapter implements IEmpleadoRestaurantePersistencePort {

    private final EmpleadoRestauranteJpaRepository empleadoRestauranteJpaRepository;

    public EmpleadoRestauranteJpaAdapter(EmpleadoRestauranteJpaRepository empleadoRestauranteJpaRepository) {
        this.empleadoRestauranteJpaRepository = empleadoRestauranteJpaRepository;
    }

    @Override
    public void saveOrUpdate(Integer idUsuario, Long idRestaurante) {
        EmpleadoRestauranteEntity entity = empleadoRestauranteJpaRepository.findByUsuario_Id(idUsuario)
                .orElseGet(EmpleadoRestauranteEntity::new);

        UsuarioEntity usuarioRef = new UsuarioEntity();
        usuarioRef.setId(idUsuario);
        entity.setUsuario(usuarioRef);
        entity.setIdRestaurante(idRestaurante);

        empleadoRestauranteJpaRepository.save(entity);
    }
}
