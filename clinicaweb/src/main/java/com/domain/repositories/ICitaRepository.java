package com.domain.repositories;

import com.domain.entities.Cita;
import java.util.List;
import java.util.Map;

public interface ICitaRepository {

    // MÉTODO CRÍTICO: Para que el controlador pueda agendar
    boolean guardarCita(Cita cita);
    boolean eliminarCita(int id_cita);
    // Para ver el historial del paciente
    List<Cita> obtenerCitasPorPaciente(int idPaciente);

    // Para que el doctor vea sus citas pendientes
    List<Cita> obtenerCitasPorDoctor(int idDoctor);

    // Para aceptar o rechazar citas
    boolean actualizarEstadoCita(int idCita, String estado);

    // Para las estadísticas (Citas totales, pendientes, etc.)
    Map<String, Object> getStatsPaciente(int idPaciente);

}