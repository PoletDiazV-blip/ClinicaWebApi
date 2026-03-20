package com.core.usecases;

import com.core.entities.User;
import com.core.ports.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Esta es la CLASE 'AuthService'.
 * Aquí se decide si el usuario entra o no a la App.
 */
public class AuthService {
    private final UserRepository userRepository;

    // Pedimos el contrato (Interfaz) para poder buscar usuarios
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        // 1. Buscamos al usuario en MySQL usando el Repositorio
        User user = userRepository.findByUsername(username);

        // 2. Si existe, comparamos la contraseña plana con el hash
        // BCrypt.checkpw hace todo el trabajo sucio por ti
        if (user != null && BCrypt.checkpw(password, user.passwordHash())) {
            return user; // ¡Login exitoso!
        }

        return null; // Si no coincide o no existe, no lo dejamos pasar
    }
}