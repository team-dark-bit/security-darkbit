package com.darkbit.security.config;

import com.darkbit.security.config.SecurityConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Configuración automática de la librería Security Darkbit.
 * Se activa automáticamente cuando se incluye la dependencia en un proyecto Spring Boot.
 *
 * @author Darkbit Security Team
 * @version 1.0.0
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.darkbit.security")
public class SecurityDarkbitAutoConfiguration {

    /**
     * Bean de ejemplo que se registra automáticamente.
     * Puedes inyectar este bean en tus servicios.
     */
    @Bean
    public SecurityConfigProperties securityConfigProperties() {
        return new SecurityConfigProperties();
    }

}

