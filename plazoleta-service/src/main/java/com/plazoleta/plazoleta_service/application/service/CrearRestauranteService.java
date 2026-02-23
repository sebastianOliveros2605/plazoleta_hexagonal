package com.plazoleta.plazoleta_service.application.service;

import org.springframework.stereotype.Service;

import com.plazoleta.plazoleta_service.domain.constants.RoleConstants;
import com.plazoleta.plazoleta_service.domain.exception.RolUsuarioNoPermitidoException;
import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearRestauranteUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrearRestauranteService implements ICrearRestauranteUseCase {
    private final IRestauranteRepositoryPort restauranteRepository;
    private final IUsuarioClientPort usuarioClientPort;

    @Override
    public void ejecutar(Restaurante restaurante) {
        restaurante.validarReglasDeNegocio();

        if (!usuarioClientPort.existeUsuario(restaurante.getIdPropietario())) {
            throw new UsuarioNoExisteException();
        }
        if (!RoleConstants.ROL_PROPIETARIO.equals(usuarioClientPort.rolUsuarioString(restaurante.getIdPropietario()))) {
            throw new RolUsuarioNoPermitidoException();
        }

        restauranteRepository.guardar(restaurante);

    }
}
