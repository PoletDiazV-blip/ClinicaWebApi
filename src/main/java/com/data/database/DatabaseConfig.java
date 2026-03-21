package com.data.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        // Cambiamos 'localhost' por tu IP Pública de AWS
        config.setJdbcUrl("jdbc:mysql://54.161.35.239:3306/thriftad_db");

        config.setUsername("root");
        config.setPassword("albafica"); // Tu contraseña maestra

        // Optimizaciones recomendadas
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Configuraciones de tiempo de espera (opcional para nubes)
        config.setConnectionTimeout(30000); // 30 segundos
        config.setIdleTimeout(600000); // 10 minutos

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}