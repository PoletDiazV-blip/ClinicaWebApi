package com.data.repositories;

import com.data.persistence.DatabaseConfig;
import com.domain.entities.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepositoryImpl {

    /**
     * LISTAR TODOS LOS DOCTORES (NUEVO)
     * Usado por el paciente para ver las cards en doctores.html
     */
    public List<Doctor> listarTodos() {
        List<Doctor> lista = new ArrayList<>();
        String sql = "SELECT id_doctor, nombre, especialidad, correo, biografia FROM doctores";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Doctor d = new Doctor();
                d.setId_doctor(rs.getInt("id_doctor"));
                d.setNombre(rs.getString("nombre") != null ? rs.getString("nombre") : "Doctor sin nombre");
                d.setEspecialidad(rs.getString("especialidad") != null ? rs.getString("especialidad") : "General");
                d.setCorreo(rs.getString("correo"));
                d.setBiografia(rs.getString("biografia"));
                lista.add(d);
            }
            System.out.println("DEBUG: Se cargaron " + lista.size() + " doctores.");
        } catch (SQLException e) {
            System.err.println("Error al listar doctores: " + e.getMessage());
        }
        return lista;
    }

    /**
     * ACTUALIZAR PERFIL COMPLETO
     * Guarda datos personales y profesionales.
     */
    public boolean actualizarPerfilCompleto(Doctor doctor) {
        String sql = "UPDATE doctores SET nombre = ?, edad = ?, sexo = ?, especialidad = ?, cedula = ?, telefono = ?, correo = ?, biografia = ? WHERE id_doctor = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getNombre());
            stmt.setInt(2, doctor.getEdad());
            stmt.setString(3, doctor.getSexo());
            stmt.setString(4, doctor.getEspecialidad());
            stmt.setString(5, doctor.getCedula());
            stmt.setString(6, doctor.getTelefono());
            stmt.setString(7, doctor.getCorreo());
            stmt.setString(8, doctor.getBiografia());
            stmt.setInt(9, doctor.getId_doctor());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en SQL al actualizar doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * OBTENER DOCTOR POR ID
     */
    public Doctor obtenerDoctorPorId(int id_doctor) {
        String sql = "SELECT * FROM doctores WHERE id_doctor = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_doctor);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Doctor d = new Doctor();
                    d.setId_doctor(rs.getInt("id_doctor"));
                    d.setId_usuario(rs.getInt("id_usuario"));
                    d.setNombre(rs.getString("nombre"));
                    d.setEdad(rs.getInt("edad"));
                    d.setSexo(rs.getString("sexo"));
                    d.setEspecialidad(rs.getString("especialidad"));
                    d.setCedula(rs.getString("cedula"));
                    d.setTelefono(rs.getString("telefono"));
                    d.setCorreo(rs.getString("correo"));
                    d.setBiografia(rs.getString("biografia"));
                    return d;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener doctor: " + e.getMessage());
        }
        return null;
    }
}