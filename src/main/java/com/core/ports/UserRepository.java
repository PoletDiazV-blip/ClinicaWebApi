package com.core.ports;

import com.core.entities.User;
import java.util.List; // Importante: agrega esta línea
/**
 * Esta es una INTERFAZ.
 * Es el contrato que define qué acciones podemos hacer con los usuarios.
 */
public interface UserRepository {

    // Aquí solo decimos QUÉ queremos: buscar un usuario usando su username.
    User findByUsername(String username);
    // BUSCAR TODOS (Para ver a los 3: Polet, Mari y Chepe)
    List<User> findAll();
}