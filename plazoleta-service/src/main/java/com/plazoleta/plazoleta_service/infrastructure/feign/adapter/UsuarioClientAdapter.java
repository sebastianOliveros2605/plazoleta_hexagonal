package com.plazoleta.plazoleta_service.infrastructure.feign.adapter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;
import com.plazoleta.plazoleta_service.infrastructure.feign.client.IUsuarioFeignClient;
import com.plazoleta.plazoleta_service.infrastructure.feign.dto.UsuarioResponse;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioClientAdapter implements IUsuarioClientPort {

    private final IUsuarioFeignClient feignClient;

    @Override
    public Long consultarIdUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException exception) {
            log.debug("No fue posible convertir el identificador de autenticacion a Long.");
            return null;
        }
    }

    @Override
    public Long consultarIdRestauranteDeUsuario(Integer idUsuario) {
        UsuarioResponse response = feignClient.obtenerUsuario(idUsuario);
        return response.getIdRestaurante();
    }

    @Override
    public String consultarCorreoUsuario(Integer idUsuario) {
        UsuarioResponse response = feignClient.obtenerUsuario(idUsuario);
        return response.getCorreo();
    }

    @Override
    public String consultarCelularUsuario(Integer idUsuario) {
        UsuarioResponse response = feignClient.obtenerUsuario(idUsuario);
        return response.getCelular();
    }

    @Override
    public String rolUsuarioString(Integer idUsuario) {
        UsuarioResponse response = feignClient.obtenerUsuario(idUsuario);
        return response.getRol();
    }

    @Override
    public Boolean existeUsuario(Integer idUsuario) {
        try {
            feignClient.obtenerUsuario(idUsuario);
            return true;
        } catch (FeignException.NotFound exception) {
            return false;
        }
    }
}
