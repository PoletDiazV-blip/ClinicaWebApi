package com.presentation.controllers;

import com.domain.entities.Cita;
import com.domain.repositories.ICitaRepository;
import io.javalin.http.Context;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CitaController {
    private final ICitaRepository citaRepository;
    private static final Logger logger = LoggerFactory.getLogger(CitaController.class);

    public CitaController(ICitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    // --- SECCIÓN PACIENTE ---

    // POST /pacientes/agendar
    public void agendarCita(Context ctx) {
        try {
            Cita nuevaCita = ctx.bodyAsClass(Cita.class);
            logger.info("Intentando agendar cita para paciente ID: {}", nuevaCita.getId_paciente());

            if (citaRepository.guardarCita(nuevaCita)) {
                ctx.status(201).json(Map.of("mensaje", "Cita agendada con éxito"));
            } else {
                ctx.status(500).json(Map.of("error", "No se pudo guardar en la BD"));
            }
        } catch (Exception e) {
            logger.error("Error al agendar: {}", e.getMessage());
            ctx.status(400).json(Map.of("error", "Datos inválidos"));
        }
    }

    // GET /pacientes/{id}/citas
    public void getCitasPorPaciente(Context ctx) {
        try {
            int idPaciente = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(citaRepository.obtenerCitasPorPaciente(idPaciente));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "ID no válido"));
        }
    }

    // DELETE /citas/{id}  <--- ¡ESTE ES EL QUE FALTABA PARA EL BOTECITO!
    public void eliminarCita(Context ctx) {
        try {
            int idCita = Integer.parseInt(ctx.pathParam("id"));
            if (citaRepository.eliminarCita(idCita)) {
                ctx.status(200).json(Map.of("mensaje", "Cita eliminada correctamente"));
            } else {
                ctx.status(500).json(Map.of("error", "No se pudo eliminar de la BD"));
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "ID de cita inválido"));
        }
    }

    // GET /pacientes/{id}/stats
    public void getStatsPaciente(Context ctx) {
        try {
            int idPaciente = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(citaRepository.getStatsPaciente(idPaciente));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Error en estadísticas"));
        }
    }

    // --- SECCIÓN DOCTOR ---

    // GET /doctores/{id}/citas
    public void getCitasPorDoctor(Context ctx) {
        try {
            int idDoctor = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(citaRepository.obtenerCitasPorDoctor(idDoctor));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "ID de doctor no válido"));
        }
    }

    // PUT /citas/{id}/estado
    public void actualizarEstado(Context ctx) {
        try {
            int idCita = Integer.parseInt(ctx.pathParam("id"));
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String nuevoEstado = body.get("estado");

            if (citaRepository.actualizarEstadoCita(idCita, nuevoEstado)) {
                ctx.status(200).json(Map.of("mensaje", "Estado actualizado"));
            } else {
                ctx.status(500).json(Map.of("error", "No se pudo actualizar"));
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Datos inválidos"));
        }
    }
}