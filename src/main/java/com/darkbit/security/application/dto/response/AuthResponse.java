package com.darkbit.security.application.dto.response;

import java.util.Set;

/**
 * DTO de respuesta para operaciones de autenticación (login/register).
 */
public class AuthResponse {

    private String token;
    private long expiresIn;
    private String username;
    private String email;
    private Set<String> roles;
    private Set<String> permissions;

    public AuthResponse() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
}

