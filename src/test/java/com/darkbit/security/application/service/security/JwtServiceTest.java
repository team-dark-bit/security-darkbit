package com.darkbit.security.application.service.security;

import com.darkbit.security.application.service.JwtService;
import java.util.Date;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

  private static final String SECRET = "my-super-secret-key-for-tests-12345";

  @Test
  void shouldGenerateValidateAndExtractClaimsFromToken() {
    JwtService jwtService = new JwtService(SECRET, 60_000);
    Date beforeGeneration = new Date();

    String token = jwtService.generateToken("alice", Set.of("ROLE_USER"), Set.of("read"));

    assertNotNull(token);
    assertTrue(jwtService.validateToken(token));
    assertTrue(beforeGeneration.before(jwtService.extractExpiration(token)));
    assertEquals("alice", jwtService.extractUsername(token));
  }

  @Test
  void shouldRejectBlankOrMalformedTokens() {
    JwtService jwtService = new JwtService(SECRET, 60_000);

    assertFalse(jwtService.validateToken(null));
    assertFalse(jwtService.validateToken("   "));
    assertFalse(jwtService.validateToken("not-a-jwt"));
  }
}
