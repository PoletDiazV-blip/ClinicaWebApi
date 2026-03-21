package com.data.repositories;

import com.core.entities.Movimiento;
import com.data.database.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoRepositoryImpl {

    public void guardar(Movimiento m) {
        String sql = "INSERT INTO movimientos (id_usuario, id_categoria, nombre_producto, monto, fecha, descripcion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getIdUsuario());
            stmt.setInt(2, m.getIdCategoria());
            stmt.setString(3, m.getNombreProducto());
            stmt.setBigDecimal(4, m.getMonto());
            stmt.setDate(5, Date.valueOf(m.getFecha()));
            stmt.setString(6, m.getDescripcion());
            stmt.executeUpdate();
            System.out.println("Guardado en AWS");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Movimiento> buscarPorUsuario(int idUsuario) {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE id_usuario = ? ORDER BY fecha DESC";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Movimiento(
                        rs.getInt("id_movimiento"), rs.getInt("id_usuario"),
                        rs.getInt("id_categoria"), rs.getString("nombre_producto"),
                        rs.getBigDecimal("monto"), rs.getDate("fecha").toLocalDate(),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM movimientos WHERE id_movimiento = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Eliminado de AWS");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}