package com.darkbit.security.application.service;

import com.darkbit.security.application.dto.request.LoginRequest;
import com.darkbit.security.application.dto.request.RegisterRequest;
import com.darkbit.security.application.dto.response.AuthResponse;
import com.darkbit.security.application.port.RolePort;
import com.darkbit.security.application.port.UserPort;
import com.darkbit.security.domain.model.RoleModel;
import com.darkbit.security.domain.model.UserModel;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Servicio de autenticación de la librería.
 * Depende de los ports UserPort y RolePort que el proyecto consumidor debe implementar.
 */
public class AuthService {

    private final UserPort userPort;
    private final RolePort rolePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserPort userPort, RolePort rolePort,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userPort = userPort;
        this.rolePort = rolePort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userPort.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userPort.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        RoleModel role = rolePort.findById(req.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + req.getRoleId()));

        UserModel user = new UserModel();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(req.getUsername());
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        userPort.save(user);

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRoleNames(),
                user.getAllPermissionNames()
        );

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setExpiresIn(TimeUnit.MILLISECONDS.toSeconds(jwtService.getExpirationMillis()));
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setRoles(user.getRoleNames());
        resp.setPermissions(user.getAllPermissionNames());
        return resp;
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String token = jwtService.generateToken(
                userDetails.getUsername(),
                userDetails.getRoleNames(),
                userDetails.getPermissionNames()
        );

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setExpiresIn(TimeUnit.MILLISECONDS.toSeconds(jwtService.getExpirationMillis()));
        resp.setUsername(userDetails.getUsername());
        resp.setEmail(userDetails.getEmail());
        resp.setRoles(userDetails.getRoleNames());
        resp.setPermissions(userDetails.getPermissionNames());
        return resp;
    }
}
