package com.presentation.controllers;

import com.data.repositories.UsuarioRepositoryImpl;
import com.domain.entities.Usuario;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private static final UsuarioRepositoryImpl repo = new UsuarioRepositoryImpl();

    public static void listar(Context ctx) {
        ctx.json(repo.listarTodos());
    }

    public static void crear(Context ctx) {
        try {
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            Usuario u = new Usuario();
            u.setCorreo(body.get("correo"));
            u.setPassword(BCrypt.hashpw(body.get("password"), BCrypt.gensalt(12)));
            u.setRol(body.get("rol").toUpperCase());

            if (repo.crearCompleto(u, body.get("nombre"), body.get("especialidad"))) {
                ctx.status(201).json(Map.of("estado", "exito", "mensaje", "Registrado correctamente"));
            } else {
                ctx.status(400).json(Map.of("error", "Error en registro"));
            }
        } catch (Exception e) { ctx.status(400).json(Map.of("error", "Datos invalidos")); }
    }

    public static void obtenerStats(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(repo.obtenerStatsDoctor(id));
        } catch (Exception e) { ctx.status(400); }
    }

    public static void obtenerPerfil(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            var perfil = repo.obtenerPerfilPaciente(id);
            if (perfil != null) ctx.json(perfil);
            else ctx.status(404);
        } catch (Exception e) { ctx.status(400); }
    }

    public static void actualizar(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Map<String, String> body = ctx.bodyAsClass(Map.class);

            boolean ok = repo.actualizarPerfilPaciente(
                    id,
                    body.get("nombre"),
                    body.get("edad"),
                    body.get("sexo"),
                    body.get("telefono")
            );

            if (ok) ctx.status(200).json(Map.of("estado", "exito", "mensaje", "Perfil actualizado"));
            else ctx.status(404).json(Map.of("error", "Usuario no encontrado"));
        } catch (Exception e) { ctx.status(400); }
    }

    public static void eliminar(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (repo.eliminar(id)) ctx.status(200).json(Map.of("mensaje", "Eliminado"));
            else ctx.status(404);
        } catch (Exception e) { ctx.status(400); }
    }
}