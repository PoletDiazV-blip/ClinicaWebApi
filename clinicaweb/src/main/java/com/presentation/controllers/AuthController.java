package com.presentation.controllers;

import com.data.repositories.UsuarioRepositoryImpl;
import com.domain.entities.Usuario;
import com.presentation.dtos.LoginRequest;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final UsuarioRepositoryImpl repository = new UsuarioRepositoryImpl();

    /**
     * Maneja el inicio de sesión comparando el Hash de BCrypt y devolviendo el ID.
     */
    public static void login(Context ctx) {
        try {
            // 1. Intentar convertir el cuerpo del JSON a la clase DTO LoginRequest
            LoginRequest loginData = ctx.bodyAsClass(LoginRequest.class);

            // 2. Validar que los campos no vengan nulos o vacíos
            if (loginData.isInvalid()) {
                logger.warn("Intento de login con datos incompletos.");
                ctx.status(400).json(Map.of("error", "Correo y contrasena son obligatorios"));
                return;
            }

            // 3. Buscar el registro del usuario en la base de datos por su correo
            Usuario usuario = repository.buscarPorCorreo(loginData.correo);

            // 4. Verificar si el usuario existe y si la contraseña coincide con el Hash
            if (usuario != null && BCrypt.checkpw(loginData.password, usuario.getPassword())) {
                logger.info("Login exitoso para el usuario: {}", usuario.getCorreo());

                // ENVIAMOS EL ID (que en tu entidad es 'id') para que el Front lo guarde
                ctx.status(200).json(Map.of(
                        "status", "exitoso",
                        "id_usuario", usuario.getId(),
                        "rol", usuario.getRol(),
                        "mensaje", "Acceso concedido"
                ));
            } else {
                // Si el usuario no existe o el password es incorrecto
                logger.warn("Credenciales inválidas para el correo: {}", loginData.correo);
                ctx.status(401).json(Map.of("error", "Usuario o contrasena invalidos"));
            }

        } catch (Exception e) {
            // Captura errores de formato JSON o problemas de conexión a la DB
            logger.error("Error crítico en el proceso de login: {}", e.getMessage());
            ctx.status(500).json(Map.of("error", "Error interno del servidor al procesar el login"));
        }
    }
}