package com.data.repositories;

import com.domain.entities.Horario;
import com.domain.repositories.IHorarioRepository;
import com.data.persistence.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioRepositoryImpl implements IHorarioRepository {

    @Override
    public boolean guardar(Horario h) {
        String sql = "INSERT INTO horarios (id_doctor, dia_semana, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
        // USA DatabaseConfig.getConnection() PARA NO ERRARLE
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, h.getId_doctor());
            stmt.setString(2, h.getDia_semana());
            stmt.setString(3, h.getHora_inicio());
            stmt.setString(4, h.getHora_fin());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Horario> obtenerPorDoctor(int id_doctor) {
        List<Horario> lista = new ArrayList<>();
        String sql = "SELECT * FROM horarios WHERE id_doctor = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_doctor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Horario h = new Horario();
                h.setId_horario(rs.getInt("id_horario"));
                h.setId_doctor(rs.getInt("id_doctor")); // <-- Agrégalo por seguridad
                h.setDia_semana(rs.getString("dia_semana"));
                h.setHora_inicio(rs.getString("hora_inicio"));
                h.setHora_fin(rs.getString("hora_fin"));
                lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean eliminar(int id_horario) {
        String sql = "DELETE FROM horarios WHERE id_horario = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_horario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}