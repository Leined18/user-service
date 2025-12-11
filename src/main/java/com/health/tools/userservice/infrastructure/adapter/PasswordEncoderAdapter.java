package com.health.tools.userservice.infrastructure.adapter;

import com.health.tools.userservice.domain.port.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * PasswordEncoderAdapter implements PasswordEncoderPort using Spring Security's
 * BCryptPasswordEncoder.
 * Provides password encoding and verification using industry-standard BCrypt
 * algorithm.
 * 
 * Bridges domain layer (port) with Spring Security framework.
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
