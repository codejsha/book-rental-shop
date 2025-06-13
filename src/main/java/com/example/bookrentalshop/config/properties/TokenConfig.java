package com.example.bookrentalshop.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.jwt")
@Getter
@RequiredArgsConstructor
public class TokenConfig {

    private final String signingKey;
    private final Duration accessTokenExpiry;
    private final Duration refreshTokenExpiry;
}
