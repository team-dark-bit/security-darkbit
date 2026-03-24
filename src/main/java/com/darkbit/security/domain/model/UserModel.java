package com.darkbit.security.domain.model;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Modelo de dominio que representa un usuario dentro de la librería.
 * No está acoplado a ninguna entidad JPA del proyecto consumidor.
 */
public class UserModel {

    private String id;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private boolean enabled;
    private LocalDateTime createdAt;
    private Set<RoleModel> roles;

    public UserModel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Set<RoleModel> getRoles() { return roles; }
    public void setRoles(Set<RoleModel> roles) { this.roles = roles; }

    /**
     * Extrae todos los nombres de permisos de todos los roles del usuario.
     */
    public Set<String> getAllPermissionNames() {
        if (roles == null) return Set.of();
        return roles.stream()
                .filter(r -> r.getPermissions() != null)
                .flatMap(r -> r.getPermissions().stream())
                .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Extrae todos los nombres de roles del usuario.
     */
    public Set<String> getRoleNames() {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(RoleModel::getName)
                .collect(java.util.stream.Collectors.toSet());
    }
}

