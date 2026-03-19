package com.darkbit.security.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad de seguridad para operaciones comunes de cifrado y validación.
 *
 * @author Darkbit Security Team
 * @version 1.0.0
 */
public class SecurityUtil {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Genera un hash SHA-256 de un texto dado.
     *
     * @param text El texto a hashear
     * @return El hash en formato hexadecimal
     * @throws IllegalArgumentException si el texto es nulo o vacío
     */
    public static String generateHash(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("El texto no puede ser nulo o vacío");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(text.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un token aleatorio seguro.
     *
     * @param length Longitud del token en bytes (será codificado en Base64)
     * @return Token aleatorio en formato Base64
     */
    public static String generateSecureToken(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser mayor a 0");
        }

        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Valida si un texto coincide con su hash.
     *
     * @param text El texto original
     * @param hash El hash a comparar
     * @return true si coinciden, false en caso contrario
     */
    public static boolean validateHash(String text, String hash) {
        if (text == null || hash == null) {
            return false;
        }

        try {
            String generatedHash = generateHash(text);
            return generatedHash.equals(hash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sanitiza un input de usuario removiendo caracteres potencialmente peligrosos.
     *
     * @param input El input a sanitizar
     * @return El input sanitizado
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Remover caracteres peligrosos para SQL injection y XSS básico
        return input.replaceAll("[<>\"'%;()&+]", "");
    }

    /**
     * Valida si un email tiene un formato válido básico.
     *
     * @param email El email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida si una contraseña cumple con requisitos mínimos de seguridad.
     * - Al menos 8 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula
     * - Al menos un número
     *
     * @param password La contraseña a validar
     * @return true si cumple los requisitos, false en caso contrario
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }

    /**
     * Convierte un array de bytes a hexadecimal.
     *
     * @param bytes Array de bytes
     * @return String en formato hexadecimal
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Enmascara información sensible dejando solo los primeros y últimos caracteres visibles.
     *
     * @param sensitiveData Datos sensibles (ej: número de tarjeta, email)
     * @param visibleChars Número de caracteres visibles al inicio y al final
     * @return String enmascarado
     */
    public static String maskSensitiveData(String sensitiveData, int visibleChars) {
        if (sensitiveData == null || sensitiveData.length() <= visibleChars * 2) {
            return "***";
        }

        String start = sensitiveData.substring(0, visibleChars);
        String end = sensitiveData.substring(sensitiveData.length() - visibleChars);
        int maskedLength = sensitiveData.length() - (visibleChars * 2);

        return start + "*".repeat(maskedLength) + end;
    }
}

