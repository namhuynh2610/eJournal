package com.bcd.ejournal.configuration.jwt;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JWTServiceConfiguration {
    private static final byte[] SECRET_KEY = "YEAH_SECRET".getBytes(StandardCharsets.UTF_8);
    private static final int DURATION = 10 * 24 * 60 * 60;

    @Bean
    JWTService getJWTService(ObjectMapper objectMapper) {
        return new JWTService(SECRET_KEY, DURATION, objectMapper);
    }
}
