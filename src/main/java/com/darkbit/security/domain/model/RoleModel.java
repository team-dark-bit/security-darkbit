package com.darkbit.security.domain.model;

import java.util.Set;

/**
 * Modelo de dominio que representa un rol dentro de la librería.
 * No está acoplado a ninguna entidad JPA del proyecto consumidor.
 * Los permisos se representan como simples Strings para máxima compatibilidad.
 */
public class RoleModel {

    private String id;
    private String name;
    private Set<String> permissions;

    public RoleModel() {}

    public RoleModel(String id, String name, Set<String> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
}

