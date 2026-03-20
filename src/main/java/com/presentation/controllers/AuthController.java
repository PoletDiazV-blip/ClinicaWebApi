package com.presentation.controllers;

import com.core.entities.User;
import com.core.usecases.AuthService;
import com.presentation.dtos.LoginRequest;
import io.javalin.http.Context;

/**
 * Esta es la CLASE 'AuthController'.
 * Es la que recibe las peticiones HTTP de Javalin.
 */
public class AuthController {
    private final AuthService authService;

    // Recibimos el servicio de autenticación para usar su lógica
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void handleLogin(Context ctx) {
        // 1. Convertimos el JSON que llega del cliente a nuestro record LoginRequest
        LoginRequest loginData = ctx.bodyAsClass(LoginRequest.class);

        // 2. Llamamos a la lógica de negocio
        User user = authService.login(loginData.username(), loginData.password());

        // 3. Si el usuario es válido, respondemos con sus datos (200 OK)
        if (user != null) {
            ctx.status(200).json(user);
        } else {
            // Si no, mandamos un error 401 (No autorizado)
            ctx.status(401).result("Usuario o contraseña incorrectos");
        }
    }
}