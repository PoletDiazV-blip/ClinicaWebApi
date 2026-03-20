package com;

import com.core.usecases.AuthService;
import com.data.repositories.UserRepositoryImpl;
import com.presentation.controllers.AuthController;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        // 1. Conectamos los cables
        var userRepo = new UserRepositoryImpl();
        var authService = new AuthService(userRepo);
        var authController = new AuthController(authService);

        // 2. Iniciamos Javalin
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        }).start(8080);

        // --- LAS RUTAS ---

        // Ruta para el Login (POST)
        app.post("/login", authController::handleLogin);

        // ESTA ES LA LÍNEA NUEVA: Ruta para ver a todos los usuarios (GET)
        // Usamos una función lambda para decirle: "cuando entren aquí, usa el repo y pásalo a JSON"
        app.get("/usuarios", ctx -> ctx.json(userRepo.findAll()));

        System.out.println("ThriftAd API Corriendo");
    }
}