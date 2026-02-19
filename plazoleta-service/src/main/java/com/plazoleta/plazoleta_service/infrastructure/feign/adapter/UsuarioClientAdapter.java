package com.plazoleta.plazoleta_service.infrastructure.feign.adapter;

import org.springframework.stereotype.Component;

import com.plazoleta.plazoleta_service.domain.ports.out.IUsuarioClientPort;
import com.plazoleta.plazoleta_service.infrastructure.feign.client.IUsuarioFeignClient;
import com.plazoleta.plazoleta_service.infrastructure.feign.dto.UsuarioResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioClientAdapter implements IUsuarioClientPort {

    private final IUsuarioFeignClient feignClient;

    @Override
    public Long consultarIdUsuario() {
        // esto depende de cómo obtengas el usuario autenticado
        return null;
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
        } catch (Exception e) {
            e.printStackTrace();   // 👈 AGREGA ESTO
            throw e; 
        }
    }
}

