package com.darkbit.security.application.port;

import com.darkbit.security.domain.model.RoleModel;

import java.util.Optional;

/**
 * Puerto (contrato) que la librería expone para operaciones sobre roles.
 * El proyecto consumidor debe implementar esta interfaz con su propio repositorio.
 */
public interface RolePort {

    Optional<RoleModel> findById(String id);
}

