package com.plazoleta.usuarios_service.infrastructure.output.feign.adapter;

import org.springframework.stereotype.Component;

import com.plazoleta.usuarios_service.domain.exception.RestauranteNoEncontradoException;
import com.plazoleta.usuarios_service.domain.puertosIn.IRestauranteClientePort;
import com.plazoleta.usuarios_service.infrastructure.output.feign.client.IPlazoletaFeignClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestauranteClienteAdapter implements IRestauranteClientePort {

    private final IPlazoletaFeignClient plazoletaFeignClient;

    @Override
    public Long consultarIdRestaurantePorPropietario(Integer idPropietario) {
        try {
            return plazoletaFeignClient.consultarIdRestaurantePorPropietario(idPropietario);
        } catch (FeignException.NotFound exception) {
            throw new RestauranteNoEncontradoException();
        }
    }
}

