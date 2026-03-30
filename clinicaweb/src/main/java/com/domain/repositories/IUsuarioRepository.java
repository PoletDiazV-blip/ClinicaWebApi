package com.domain.repositories;

import com.domain.entities.Usuario;

/**
 * Principio de Inversión de Dependencias (SOLID):
 * El dominio define QUÉ necesita, pero no CÓMO se obtiene.
 */
public interface IUsuarioRepository {
    // Definimos que cualquier repositorio de usuarios debe poder buscar por correo
    Usuario buscarPorCorreo(String correo);
}