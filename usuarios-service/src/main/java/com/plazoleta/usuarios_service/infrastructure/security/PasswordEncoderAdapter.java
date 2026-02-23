package com.plazoleta.usuarios_service.infrastructure.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordEncoderAdapter implements IPasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    
}
