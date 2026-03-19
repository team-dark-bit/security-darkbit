package com.darkbit.security.application.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey secretKey;
  private final long expirationMillis;

  public JwtService(@Value("${jwt.secret:default_jwt_secret}") String secret,
                    @Value("${jwt.expiration:259200000}") long expirationMillis) {
    // Ensure secret is long enough for HS256
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes.length < 32 ? padTo32Bytes(keyBytes) : keyBytes);
    this.expirationMillis = expirationMillis;
  }

  private static byte[] padTo32Bytes(byte[] src) {
    byte[] target = new byte[32];
    System.arraycopy(src, 0, target, 0, Math.min(src.length, target.length));
    for (int i = src.length; i < target.length; i++) target[i] = 0;
    return target;
  }

  public String generateToken(String username, Set<String> roles, Set<String> permissions) {
    long now = System.currentTimeMillis();
    Date issuedAt = new Date(now);
    Date expiration = new Date(now + expirationMillis);

    return Jwts.builder()
            .setSubject(username)
            .claim("roles", roles)
            .claim("perms", permissions)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            // use the SecretKey directly; avoids using deprecated SignatureAlgorithm overloads
            .signWith(secretKey)
            .compact();
  }

  public boolean validateToken(String token) {
    try {
      // Use parser() and build() to obtain a JwtParser compatible con la versión disponible
      Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String extractUsername(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public Date extractExpiration(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    return claims.getExpiration();
  }
}
