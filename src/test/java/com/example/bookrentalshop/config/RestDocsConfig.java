package com.example.bookrentalshop.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@Getter
public class RestDocsConfig {

    @Value("${app.restdocs.scheme}")
    private String scheme;

    @Value("${app.restdocs.host}")
    private String host;

    @Value("${app.restdocs.port}")
    private Integer port;
}
