package com.plazoleta.usuarios_service.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.plazoleta.usuarios_service.domain.puertosIn.IPasswordEncoderPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements IPasswordEncoderPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    
}
