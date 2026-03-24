package com.darkbit.security.application.dto.request;

/**
 * DTO de petición para el registro de un nuevo usuario.
 */
public class RegisterRequest {

    private String username;
    private String fullName;
    private String email;
    private String password;
    private String roleId;

    public RegisterRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
}

