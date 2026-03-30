package com.data.repositories;

import com.data.persistence.DatabaseConfig;
import com.domain.entities.Usuario;
import com.domain.repositories.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.*;

public class UsuarioRepositoryImpl implements IUsuarioRepository {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioRepositoryImpl.class);

    @Override
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException e) { logger.error("Error buscarPorCorreo: {}", e.getMessage()); }
        return null;
    }

    public boolean crearCompleto(Usuario u, String nombre, String especialidad) {
        String sqlUser = "INSERT INTO usuarios (correo, password, rol) VALUES (?, ?, ?)";
        String sqlDoc = "INSERT INTO doctores (id_usuario, nombre, especialidad, cedula) VALUES (?, ?, ?, 'POR_DEFINIR')";
        String sqlPac = "INSERT INTO pacientes (id_usuario, nombre) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement stUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            stUser.setString(1, u.getCorreo());
            stUser.setString(2, u.getPassword());
            stUser.setString(3, u.getRol().toUpperCase());
            stUser.executeUpdate();

            ResultSet rs = stUser.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("ID no generado");
            int idGenerado = rs.getInt(1);

            boolean esDoctor = u.getRol().equalsIgnoreCase("DOCTOR");
            PreparedStatement stHija = conn.prepareStatement(esDoctor ? sqlDoc : sqlPac);
            stHija.setInt(1, idGenerado);
            stHija.setString(2, nombre);
            if (esDoctor) stHija.setString(3, (especialidad == null || especialidad.isEmpty()) ? "General" : especialidad);

            stHija.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    public Map<String, Object> obtenerPerfilPaciente(int idUsuario) {
        String sql = "SELECT u.correo, p.nombre, p.edad, p.sexo, p.telefono FROM usuarios u " +
                "JOIN pacientes p ON u.id_usuario = p.id_usuario WHERE u.id_usuario = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("correo", rs.getString("correo"));
                p.put("nombre", rs.getString("nombre"));
                p.put("edad", rs.getString("edad"));
                p.put("sexo", rs.getString("sexo"));
                p.put("telefono", rs.getString("telefono"));
                return p;
            }
        } catch (SQLException e) { logger.error("Error obtenerPerfilPaciente: {}", e.getMessage()); }
        return null;
    }

    public boolean actualizarPerfilPaciente(int idUsuario, String nombre, String edad, String sexo, String profesional) {
        String sql = "UPDATE pacientes SET nombre = ?, edad = ?, sexo = ?, telefono = ? WHERE id_usuario = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, edad);
            stmt.setString(3, sexo);
            stmt.setString(4, profesional); // Usamos el campo teléfono de tu HTML
            stmt.setInt(5, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { logger.error("Error actualizarPerfilPaciente: {}", e.getMessage()); return false; }
    }

    public Map<String, Integer> obtenerStatsDoctor(int idUsuario) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM citas c JOIN doctores d ON c.id_doctor = d.id_doctor WHERE d.id_usuario = ? AND DATE(c.fecha) = CURDATE() AND c.estado = 'ACEPTADA') as hoy, " +
                "(SELECT COUNT(*) FROM citas c JOIN doctores d ON c.id_doctor = d.id_doctor WHERE d.id_usuario = ? AND c.estado = 'PENDIENTE') as pendientes";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUsuario); st.setInt(2, idUsuario);
            ResultSet rs = st.executeQuery();
            if (rs.next()) { stats.put("hoy", rs.getInt("hoy")); stats.put("pendientes", rs.getInt("pendientes")); }
        } catch (SQLException e) { logger.error("Error stats: {}", e.getMessage()); }
        return stats;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) usuarios.add(mapearUsuario(rs));
        } catch (SQLException e) { logger.error("Error listarTodos: {}", e.getMessage()); }
        return usuarios;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id_usuario"), rs.getString("correo"), rs.getString("password"), rs.getString("rol"));
    }
}