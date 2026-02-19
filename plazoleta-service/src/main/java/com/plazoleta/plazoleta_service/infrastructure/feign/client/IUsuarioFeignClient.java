package com.plazoleta.plazoleta_service.infrastructure.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.plazoleta.plazoleta_service.infrastructure.feign.config.FeignConfig;
import com.plazoleta.plazoleta_service.infrastructure.feign.dto.UsuarioResponse;


@FeignClient(name = "usuarios-service", url = "${usuarios.service.url}", configuration = FeignConfig.class)
public interface IUsuarioFeignClient {

    @GetMapping("/usuarios/{idUsuario}")
    UsuarioResponse obtenerUsuario(@PathVariable Integer idUsuario);

}

