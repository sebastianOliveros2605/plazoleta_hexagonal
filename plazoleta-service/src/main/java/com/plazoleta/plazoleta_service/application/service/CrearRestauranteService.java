package com.plazoleta.plazoleta_service.application.service;

import org.springframework.stereotype.Service;

import com.plazoleta.plazoleta_service.domain.exception.NitInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.NombreInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.RolUsuarioNoPermitidoException;
import com.plazoleta.plazoleta_service.domain.exception.TelefonoInvalidoException;
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
        if (!usuarioClientPort.existeUsuario(restaurante.getIdPropietario())) {
            throw new UsuarioNoExisteException();
        }
        if (!usuarioClientPort.rolUsuarioString(restaurante.getIdPropietario()).equals("PROPIETARIO")) {
            throw new RolUsuarioNoPermitidoException();
        }
        validarNombre(restaurante.getNombre());
        validarNit(restaurante.getNit());
        validarTelefono(restaurante.getTelefono());

        restauranteRepository.guardar(restaurante);

    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new NombreInvalidoException();
        }
        if (nombre.matches("\\d+")) {
            throw new NombreInvalidoException();
        }
    }

    private void validarNit(String nit) {
        if (!nit.matches("\\d+")) {
            throw new NitInvalidoException();
        }
    }

    private void validarTelefono(String telefono) {
        if (telefono.length() > 13) {
            throw new TelefonoInvalidoException();
        }
    }
}
