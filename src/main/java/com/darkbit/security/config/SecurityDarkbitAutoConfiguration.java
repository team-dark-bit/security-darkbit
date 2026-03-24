package com.darkbit.security.config;

import com.darkbit.security.application.port.RolePort;
import com.darkbit.security.application.port.UserPort;
import com.darkbit.security.application.service.AuthService;
import com.darkbit.security.application.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración automática de la librería Security Darkbit.
 * Se activa automáticamente cuando se incluye la dependencia en un proyecto Spring Boot.
 *
 * @author Darkbit Security Team
 * @version 1.0.0
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class SecurityDarkbitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtService jwtService(
            @Value("${jwt.secret:default_jwt_secret}") String secret,
            @Value("${jwt.expiration:259200000}") long expirationMillis) {
        return new JwtService(secret, expirationMillis);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({UserPort.class, RolePort.class, PasswordEncoder.class, AuthenticationManager.class})
    public AuthService authService(UserPort userPort,
                                   RolePort rolePort,
                                   PasswordEncoder passwordEncoder,
                                   JwtService jwtService,
                                   AuthenticationManager authenticationManager) {
        return new AuthService(userPort, rolePort, passwordEncoder, jwtService, authenticationManager);
    }
}
