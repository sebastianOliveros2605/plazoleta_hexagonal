package com.plazoleta.usuarios_service.infrastructure.output.feign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.plazoleta.usuarios_service.infrastructure.security.SecurityConstants;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                String authHeader = attributes.getRequest().getHeader(SecurityConstants.ENCABEZADO_AUTORIZACION);
                if (authHeader != null) {
                    requestTemplate.header(SecurityConstants.ENCABEZADO_AUTORIZACION, authHeader);
                }
            }
        };
    }
}
