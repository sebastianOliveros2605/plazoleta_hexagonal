package com.plazoleta.plazoleta_service.infrastructure.feign.client;

import com.plazoleta.plazoleta_service.infrastructure.feign.config.FeignConfig;
import com.plazoleta.plazoleta_service.infrastructure.feign.dto.TrazabilidadRequest;
import com.plazoleta.plazoleta_service.infrastructure.feign.dto.TrazabilidadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "trazabilidadClient",
        url = "${trazabilidad.service.url}",
        configuration = FeignConfig.class)
public interface ITrazabilidadFeignClient {

    @PostMapping("/trazabilidad")
    void registrar(@RequestBody TrazabilidadRequest request);

    @GetMapping("/trazabilidad/restaurante/{idRestaurante}")
    List<TrazabilidadResponse> consultarPorRestaurante(@PathVariable Long idRestaurante);

    @GetMapping("/trazabilidad/pedido/{idPedido}")
    List<TrazabilidadResponse> consultarPorPedido(@PathVariable Long idPedido);
}
