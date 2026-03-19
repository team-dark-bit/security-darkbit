package com.darkbit.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de configuración para la librería Security Darkbit.
 * Se pueden configurar en application.properties o application.yml.
 *
 * Ejemplo de uso en application.yml:
 * <pre>
 * security:
 *   darkbit:
 *     enabled: true
 *     token-length: 32
 * </pre>
 *
 * @author Darkbit Security Team
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "security.darkbit")
public class SecurityConfigProperties {

    /**
     * Habilita o deshabilita las funcionalidades de seguridad.
     */
    private boolean enabled = true;

    /**
     * Longitud predeterminada para tokens generados.
     */
    private int tokenLength = 32;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getTokenLength() {
        return tokenLength;
    }

    public void setTokenLength(int tokenLength) {
        this.tokenLength = tokenLength;
    }
}

