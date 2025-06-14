package com.example.bookrentalshop.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "com.example.bookrentalshop.config.properties")
public class PropertyConfig {
}
