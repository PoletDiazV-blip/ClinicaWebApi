package com;

import com.core.usecases.*;
import com.data.repositories.UserRepositoryImpl;
import com.data.repositories.MovimientoRepositoryImpl;
import com.presentation.controllers.AuthController;
import com.presentation.controllers.MovimientoController;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        // 1. CONFIGURACIÓN DE USUARIOS (LOGIN)
        var userRepo = new UserRepositoryImpl();
        var authService = new AuthService(userRepo);
        var authController = new AuthController(authService);

        // 2. CONFIGURACIÓN DE MOVIMIENTOS
        var movRepo = new MovimientoRepositoryImpl();

        // Casos de Uso (Brains)
        var ucRegistrar = new RegistrarMovimiento(movRepo);
        var ucObtener = new ObtenerMovimientos(movRepo);
        var ucEliminar = new EliminarMovimiento(movRepo); // <-- Agregamos este para borrar errores

        // Controlador (Puerta)
        var movController = new MovimientoController(ucRegistrar, ucObtener, ucEliminar);

        // 3. INICIO DE JAVALIN
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        }).start(8080);

        //  LAS RUTAS

        // Bienvenida
        app.get("/", ctx -> ctx.result("ThriftAd API Corriendo"));

        // Rutas de Usuario
        app.post("/login", authController::handleLogin);
        app.get("/usuarios", ctx -> ctx.json(userRepo.findAll()));

        // Rutas de Movimientos
        app.post("/movimientos", movController::crear);           // GUARDAR
        app.get("/movimientos/{id}", movController::obtenerPorUsuario); // VER LISTA
        app.delete("/movimientos/{id}", movController::eliminar); // BORRAR (El que faltaba)

        System.out.println("ThriftAd API Corriendo");
    }
}