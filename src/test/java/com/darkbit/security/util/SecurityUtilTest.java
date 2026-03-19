package com.darkbit.security.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para SecurityUtil.
 */
class SecurityUtilTest {

    @Test
    void testGenerateHash() {
        String text = "test";
        String hash = SecurityUtil.generateHash(text);

        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 produce 64 caracteres hex
    }

    @Test
    void testGenerateHashThrowsExceptionForNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            SecurityUtil.generateHash(null);
        });
    }

    @Test
    void testGenerateHashThrowsExceptionForEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            SecurityUtil.generateHash("");
        });
    }

    @Test
    void testGenerateSecureToken() {
        String token = SecurityUtil.generateSecureToken(32);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGenerateSecureTokenThrowsExceptionForInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            SecurityUtil.generateSecureToken(0);
        });
    }

    @Test
    void testValidateHash() {
        String text = "password";
        String hash = SecurityUtil.generateHash(text);

        assertTrue(SecurityUtil.validateHash(text, hash));
        assertFalse(SecurityUtil.validateHash("wrongpassword", hash));
    }

    @Test
    void testValidateHashReturnsFalseForNull() {
        assertFalse(SecurityUtil.validateHash(null, "hash"));
        assertFalse(SecurityUtil.validateHash("text", null));
    }

    @Test
    void testSanitizeInput() {
        String input = "<script>alert('xss')</script>";
        String sanitized = SecurityUtil.sanitizeInput(input);

        assertFalse(sanitized.contains("<"));
        assertFalse(sanitized.contains(">"));
    }

    @Test
    void testSanitizeInputReturnsNullForNull() {
        assertNull(SecurityUtil.sanitizeInput(null));
    }

    @Test
    void testIsValidEmail() {
        assertTrue(SecurityUtil.isValidEmail("test@example.com"));
        assertTrue(SecurityUtil.isValidEmail("user.name@domain.co.uk"));

        assertFalse(SecurityUtil.isValidEmail("invalid"));
        assertFalse(SecurityUtil.isValidEmail("@example.com"));
        assertFalse(SecurityUtil.isValidEmail("test@"));
        assertFalse(SecurityUtil.isValidEmail(null));
        assertFalse(SecurityUtil.isValidEmail(""));
    }

    @Test
    void testIsStrongPassword() {
        assertTrue(SecurityUtil.isStrongPassword("Password123"));
        assertTrue(SecurityUtil.isStrongPassword("MyP@ssw0rd"));

        assertFalse(SecurityUtil.isStrongPassword("short")); // muy corta
        assertFalse(SecurityUtil.isStrongPassword("password")); // sin mayúsculas ni números
        assertFalse(SecurityUtil.isStrongPassword("PASSWORD123")); // sin minúsculas
        assertFalse(SecurityUtil.isStrongPassword("Password")); // sin números
        assertFalse(SecurityUtil.isStrongPassword(null));
    }

    @Test
    void testMaskSensitiveData() {
        String data = "1234567890";
        String masked = SecurityUtil.maskSensitiveData(data, 2);

        assertEquals("12******90", masked);
        assertTrue(masked.startsWith("12"));
        assertTrue(masked.endsWith("90"));
    }

    @Test
    void testMaskSensitiveDataReturnsAsterisksForShortData() {
        String data = "123";
        String masked = SecurityUtil.maskSensitiveData(data, 2);

        assertEquals("***", masked);
    }

    @Test
    void testMaskSensitiveDataReturnsAsterisksForNull() {
        String masked = SecurityUtil.maskSensitiveData(null, 2);
        assertEquals("***", masked);
    }
}

