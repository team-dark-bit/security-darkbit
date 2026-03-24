package com.darkbit.security.application.port;

import com.darkbit.security.domain.model.UserModel;

import java.util.Optional;

/**
 * Puerto (contrato) que la librería expone para operaciones sobre usuarios.
 * El proyecto consumidor debe implementar esta interfaz con su propio repositorio.
 */
public interface UserPort {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserModel> findByUsername(String username);

    void save(UserModel user);
}

