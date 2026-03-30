package com.data.repositories;

import com.domain.entities.Cita;
import com.domain.repositories.ICitaRepository;
import com.data.persistence.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitaRepositoryImpl implements ICitaRepository {

    // --- MÓDULO DOCTOR ---
    @Override
    public List<Cita> obtenerCitasPorDoctor(int id_doctor) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.id_cita, c.id_paciente, c.id_doctor, c.motivo_cita, c.estado, c.fecha, c.hora, p.nombre AS nombre_paciente " +
                "FROM citas c " +
                "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                "WHERE c.id_doctor = ? " +
                "ORDER BY c.fecha ASC, c.hora ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_doctor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cita cita = mapearCita(rs);
                    cita.setNombre_paciente(rs.getString("nombre_paciente"));
                    citas.add(cita);
                }
            }
        } catch (SQLException e) { System.err.println("Error obtenerCitasPorDoctor: " + e.getMessage()); }
        return citas;
    }

    // --- MÓDULO PACIENTE (GUARDAR) ---
    @Override
    public boolean guardarCita(Cita cita) {
        String sql = "INSERT INTO citas (id_paciente, id_doctor, id_horario, motivo_cita, estado, fecha, hora) VALUES (?, ?, ?, ?, 'PENDIENTE', ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cita.getId_paciente());
            stmt.setInt(2, cita.getId_doctor());
            stmt.setInt(3, cita.getId_cita());
            stmt.setString(4, (cita.getMotivo_cita() != null) ? cita.getMotivo_cita() : "Consulta General");
            stmt.setString(5, cita.getFecha());
            stmt.setString(6, cita.getHora());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error guardarCita (SQL): " + e.getMessage());
            return false;
        }
    }

    // --- MÓDULO PACIENTE (LISTAR ESTADOS) ---
    @Override
    public List<Cita> obtenerCitasPorPaciente(int id_paciente) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, d.nombre AS nombre_doctor FROM citas c " +
                "INNER JOIN doctores d ON c.id_doctor = d.id_doctor " +
                "WHERE c.id_paciente = ? ORDER BY c.id_cita DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_paciente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cita cita = mapearCita(rs);
                    cita.setNombre_doctor(rs.getString("nombre_doctor"));
                    citas.add(cita);
                }
            }
        } catch (SQLException e) { System.err.println("Error obtenerCitasPorPaciente: " + e.getMessage()); }
        return citas;
    }

    // --- MÓDULO PACIENTE (BORRAR CITA) - ¡AÑADIDO! ---
    @Override
    public boolean eliminarCita(int id_cita) {
        String sql = "DELETE FROM citas WHERE id_cita = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_cita);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminarCita: " + e.getMessage());
            return false;
        }
    }

    // --- MÓDULO PACIENTE (ESTADÍSTICAS) ---
    @Override
    public Map<String, Object> getStatsPaciente(int id_paciente) {
        Map<String, Object> stats = new HashMap<>();
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM citas WHERE id_paciente = ?) as total, " +
                "(SELECT CONCAT(fecha, ' - ', hora) FROM citas WHERE id_paciente = ? AND estado = 'ACEPTADA' ORDER BY id_cita DESC LIMIT 1) as proxima, " +
                "(SELECT estado FROM citas WHERE id_paciente = ? ORDER BY id_cita DESC LIMIT 1) as ultimo_estado";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_paciente);
            stmt.setInt(2, id_paciente);
            stmt.setInt(3, id_paciente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalCitas", rs.getInt("total"));
                    stats.put("proximaCita", rs.getString("proxima") != null ? rs.getString("proxima") : "Sin citas programadas");
                    stats.put("ultimoEstado", rs.getString("ultimo_estado") != null ? rs.getString("ultimo_estado") : "N/A");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }

    @Override
    public boolean actualizarEstadoCita(int id_cita, String nuevo_estado) {
        String sql = "UPDATE citas SET estado = ? WHERE id_cita = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevo_estado.toUpperCase());
            stmt.setInt(2, id_cita);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    private Cita mapearCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setId_cita(rs.getInt("id_cita"));
        cita.setId_paciente(rs.getInt("id_paciente"));
        cita.setId_doctor(rs.getInt("id_doctor"));
        cita.setMotivo_cita(rs.getString("motivo_cita") != null ? rs.getString("motivo_cita") : "Sin motivo");
        cita.setEstado(rs.getString("estado"));
        cita.setFecha(rs.getString("fecha"));
        cita.setHora(rs.getString("hora"));
        return cita;
    }
}