package com.darkbit;

import com.darkbit.security.util.SecurityUtil;

/**
 * Clase de ejemplo para demostrar el uso de la librería Security Darkbit.
 * Esta clase NO es necesaria para usar la librería, es solo un ejemplo.
 *
 * Para usar esta librería en otro proyecto:
 * 1. Compila este proyecto con: mvn clean install
 * 2. Agrega la dependencia en el pom.xml de tu proyecto:
 *    <dependency>
 *        <groupId>com.darkbit</groupId>
 *        <artifactId>security-darkbit</artifactId>
 *        <version>1.0.0</version>
 *    </dependency>
 * 3. Usa las utilidades: SecurityUtil.generateHash("texto")
 */
public class SecurityLibraryExample {

    public static void main(String[] args) {
        // Ejemplos de uso de la librería
        System.out.println("=== Security Darkbit Library - Ejemplos de Uso ===\n");

        // Ejemplo 1: Generar hash
        String texto = "MiContraseñaSecreta";
        String hash = SecurityUtil.generateHash(texto);
        System.out.println("1. Hash SHA-256:");
        System.out.println("   Texto: " + texto);
        System.out.println("   Hash: " + hash + "\n");

        // Ejemplo 2: Generar token seguro
        String token = SecurityUtil.generateSecureToken(32);
        System.out.println("2. Token Seguro:");
        System.out.println("   Token: " + token + "\n");

        // Ejemplo 3: Validar email
        String email = "usuario@ejemplo.com";
        boolean esEmailValido = SecurityUtil.isValidEmail(email);
        System.out.println("3. Validación de Email:");
        System.out.println("   Email: " + email);
        System.out.println("   Es válido: " + esEmailValido + "\n");

        // Ejemplo 4: Validar contraseña fuerte
        String password = "MiPassword123";
        boolean esPasswordFuerte = SecurityUtil.isStrongPassword(password);
        System.out.println("4. Validación de Contraseña:");
        System.out.println("   Password: " + password);
        System.out.println("   Es fuerte: " + esPasswordFuerte + "\n");

        // Ejemplo 5: Enmascarar datos sensibles
        String numeroTarjeta = "1234567890123456";
        String enmascarado = SecurityUtil.maskSensitiveData(numeroTarjeta, 4);
        System.out.println("5. Enmascarar Datos:");
        System.out.println("   Original: " + numeroTarjeta);
        System.out.println("   Enmascarado: " + enmascarado + "\n");

        // Ejemplo 6: Validar hash
        boolean hashValido = SecurityUtil.validateHash(texto, hash);
        System.out.println("6. Validar Hash:");
        System.out.println("   Hash coincide: " + hashValido);
    }
}
