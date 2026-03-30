package com.presentation.controllers;

import com.data.repositories.DoctorRepositoryImpl;
import com.domain.entities.Doctor;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.List;

public class DoctorController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorRepositoryImpl doctorRepo;

    public DoctorController(DoctorRepositoryImpl doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    /**
     * GET: Listar todos los doctores
     * NUEVO: Usado por el paciente en doctores.html
     */
    public void listarDoctores(Context ctx) {
        try {
            List<Doctor> lista = doctorRepo.listarTodos();
            if (lista.isEmpty()) {
                ctx.status(204); // No Content pero exitoso
            }
            ctx.json(lista);
        } catch (Exception e) {
            logger.error("Error al listar doctores: {}", e.getMessage());
            ctx.status(500).json(Map.of("error", "No se pudo obtener la lista de doctores"));
        }
    }

    /**
     * GET: Obtener perfil por ID
     */
    public void obtenerPerfil(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Doctor d = doctorRepo.obtenerDoctorPorId(id);

            if (d != null) {
                ctx.json(d);
            } else {
                ctx.status(404).json(Map.of("error", "Doctor no encontrado"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("error", "ID de doctor no válido"));
        }
    }

    /**
     * PUT: Actualizar perfil completo
     */
    public void actualizarPerfil(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Doctor doctorData = ctx.bodyAsClass(Doctor.class);
            doctorData.setId_doctor(id);

            // --- BLOQUE DE BLINDAJE ---
            if (doctorData.getNombre() == null || doctorData.getNombre().isBlank()) {
                ctx.status(400).json(Map.of("error", "El nombre es obligatorio"));
                return;
            }
            if (doctorData.getEspecialidad() == null || doctorData.getEspecialidad().isBlank()) {
                ctx.status(400).json(Map.of("error", "La especialidad es obligatoria"));
                return;
            }

            if (doctorRepo.actualizarPerfilCompleto(doctorData)) {
                logger.info("Perfil del doctor {} actualizado con éxito", id);
                ctx.json(Map.of("mensaje", "Perfil actualizado correctamente", "status", "success"));
            } else {
                ctx.status(500).json(Map.of("error", "Error interno al guardar en la DB"));
            }

        } catch (Exception e) {
            logger.error("Error crítico en DoctorController: {}", e.getMessage());
            ctx.status(400).json(Map.of("error", "Formato de datos incorrecto"));
        }
    }
}