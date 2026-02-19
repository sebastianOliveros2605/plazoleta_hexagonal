package com.plazoleta.usuarios_service.domain.puertosIn;

public interface IPasswordEncoderPort {
    String encode(String rawPassword);
    Boolean matches(String rawPassword, String encodedPassword);
}
