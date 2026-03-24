package com.darkbit.security.application.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementación de UserDetails usada internamente por la librería.
 * Encapsula username, email, roles y permisos para el contexto de seguridad de Spring.
 */
public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final Set<String> roleNames;
    private final Set<String> permissionNames;

    public CustomUserDetails(String username, String email, String password, boolean enabled,
                             Set<String> roleNames, Set<String> permissionNames) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roleNames = roleNames != null ? roleNames : Set.of();
        this.permissionNames = permissionNames != null ? permissionNames : Set.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Roles con prefijo ROLE_ + permisos directos
        return Stream.concat(
                roleNames.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)),
                permissionNames.stream().map(SimpleGrantedAuthority::new)
        ).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }

    public String getEmail() { return email; }

    public Set<String> getRoleNames() { return roleNames; }

    public Set<String> getPermissionNames() { return permissionNames; }
}

