package com.darkbit.security.application.service;

import com.anthares.application.dto.request.LoginRequest;
import com.anthares.application.dto.request.RegisterRequest;
import com.anthares.application.dto.response.AuthResponse;
import com.anthares.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.anthares.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.anthares.infrastructure.adapter.out.persistence.repository.RoleRepository;
import com.anthares.infrastructure.adapter.out.persistence.repository.UserRepository;
import com.darkbit.security.application.service.security.JwtService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Validar que el rol exista
        RoleEntity role = roleRepository.findById(req.getRoleId())
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(req.getUsername());
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        userRepository.save(user);

        AuthResponse resp = new AuthResponse();
        // UserEntity doesn't expose getRoleNames(), construir el conjunto de nombres de roles aquí
        java.util.Set<String> roleNames = user.getRoles().stream().map(RoleEntity::getName).collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> permissionNames = user.getAllPermissions().stream().map(com.anthares.infrastructure.adapter.out.persistence.entity.PermissionEntity::getName).collect(java.util.stream.Collectors.toSet());
        String token = jwtService.generateToken(user.getUsername(), roleNames, permissionNames);
        resp.setToken(token);
        resp.setExpiresIn(java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(System.getProperty("jwt.expiration", "259200000"))));
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setRoles(user.getRoles().stream().map(RoleEntity::getName).collect(java.util.stream.Collectors.toSet()));
        resp.setPermissions(user.getAllPermissions().stream().map(com.anthares.infrastructure.adapter.out.persistence.entity.PermissionEntity::getName).collect(java.util.stream.Collectors.toSet()));
        return resp;
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        AuthResponse resp = new AuthResponse();
        String token = jwtService.generateToken(userDetails.getUsername(), userDetails.getRoleNames(), userDetails.getPermissionNames());
        resp.setToken(token);
        resp.setExpiresIn(java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(System.getProperty("jwt.expiration", "259200000"))));
        resp.setUsername(userDetails.getUsername());
        resp.setEmail(userDetails.getEmail());
        resp.setRoles(userDetails.getRoleNames());
        resp.setPermissions(userDetails.getPermissionNames());
        // update last login
        return resp;
    }
}
