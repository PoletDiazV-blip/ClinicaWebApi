package com.data.repositories;

import com.core.entities.User;
import com.core.ports.UserRepository;
import com.data.database.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList; // Para la lista de los 3 usuarios
import java.util.List;

/**
 * Esta es la CLASE 'UserRepositoryImpl'.
 * Aquí es donde realmente sucede la magia con MySQL.
 */
public class UserRepositoryImpl implements UserRepository {

    // --- MÉTODO 1: PARA EL LOGIN ---
    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("nombre_completo"),
                        rs.getInt("id_rol"),
                        rs.getDouble("presupuesto_disponible")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- MÉTODO 2: PARA EL CRUD (LISTAR TODOS) ---
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        // Traemos todo de la tabla usuarios
        String sql = "SELECT id_usuario, username, nombre_completo, id_rol, presupuesto_disponible FROM usuarios";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Metemos a cada usuario (Polet, Mari, Chepe) a la lista
                users.add(new User(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        null, // Por seguridad no mandamos el hash en la lista pública
                        rs.getString("nombre_completo"),
                        rs.getInt("id_rol"),
                        rs.getDouble("presupuesto_disponible")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

