package com.darkbit.security.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;

public class JwtService {

  private static final String ROLES_CLAIM = "roles";
  private static final String PERMISSIONS_CLAIM = "perms";

  private final SecretKey secretKey;
  private final long expirationMillis;

  public JwtService(String secret, long expirationMillis) {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes.length < 32 ? padTo32Bytes(keyBytes) : keyBytes);
    this.expirationMillis = expirationMillis;
  }

  private static byte[] padTo32Bytes(byte[] src) {
    byte[] target = new byte[32];
    System.arraycopy(src, 0, target, 0, Math.min(src.length, target.length));
    for (int i = src.length; i < target.length; i++) {
      target[i] = 0;
    }
    return target;
  }

  public long getExpirationMillis() {
    return expirationMillis;
  }

  public String generateToken(String username, Set<String> roles, Set<String> permissions) {
    long now = System.currentTimeMillis();
    Date issuedAt = new Date(now);
    Date expiration = new Date(now + expirationMillis);

    return Jwts.builder()
        .subject(username)
        .claim(ROLES_CLAIM, roles)
        .claim(PERMISSIONS_CLAIM, permissions)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    if (token == null || token.isBlank()) {
      return false;
    }

    try {
      parseClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException ex) {
      return false;
    }
  }

  public String extractUsername(String token) {
    return parseClaims(token).getSubject();
  }

  @SuppressWarnings("unused")
  public Date extractExpiration(String token) {
    return parseClaims(token).getExpiration();
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
