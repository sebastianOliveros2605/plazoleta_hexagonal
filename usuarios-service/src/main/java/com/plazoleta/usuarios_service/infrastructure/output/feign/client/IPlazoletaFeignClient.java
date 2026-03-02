package com.plazoleta.usuarios_service.infrastructure.output.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.plazoleta.usuarios_service.infrastructure.output.feign.config.FeignConfig;

@FeignClient(name = "plazoleta-service", url = "${plazoleta.service.url}", configuration = FeignConfig.class)
public interface IPlazoletaFeignClient {

    @GetMapping("/restaurantes/propietario/{idPropietario}")
    Long consultarIdRestaurantePorPropietario(@PathVariable Integer idPropietario);
}

