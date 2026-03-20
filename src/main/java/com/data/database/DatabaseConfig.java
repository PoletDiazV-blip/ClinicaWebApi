package com.data.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        // Configuración para tu MySQL Workbench Local
        config.setJdbcUrl("jdbc:mysql://localhost:3306/thriftad_db");
        config.setUsername("root"); // Pon aquí tu usuario de Workbench
        config.setPassword("albafica"); // ¡Pon aquí TU CONTRASEÑA de Workbench!

        // Optimizaciones recomendadas
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource = new HikariDataSource(config);
    }

    // Este método es el que usaremos para pedir una "llave" a la base de datos
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}