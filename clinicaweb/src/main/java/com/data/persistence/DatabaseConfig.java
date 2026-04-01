package com.data.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;

    static {
        try {
            logger.info("Iniciando configuración del pool HikariCP para AIVEN Cloud...");

            HikariConfig config = new HikariConfig();

            // --- NUEVA CONFIGURACIÓN DE AIVEN ---
            // Reemplaza 'TU_PASSWORD_AQUÍ' con la que copiaste del "ojito" en Aiven
            config.setJdbcUrl("jdbc:mysql://mysql-289c6c6b-itandiaz678-4012.b.aivencloud.com:20188/defaultdb?ssl-mode=REQUIRED");
            config.setUsername("avnadmin");
            config.setPassword("AVNS_FUZVYx4eSZnQN4CzO-0");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // Optimizaciones recomendadas para MySQL en la nube
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");

            // Configuraciones de Pool (Mantenerlas igual, son buenas)
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);

            // Prueba de fuego: Validar conexión a la nube
            try (Connection testConn = dataSource.getConnection()) {
                logger.info("CONEXIÓN EXITOSA ClinicApp");
            }

        } catch (SQLException e) {
            logger.error("ERROR CRITICO: No se pudo conectar a la base de datos en AIVEN.");
            logger.error("Detalle del error: {}", e.getMessage());
            throw new RuntimeException("Fallo al inicializar la base de datos en la nube", e);
        } catch (Exception e) {
            logger.error("ERROR INESPERADO al configurar HikariCP: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("El DataSource no ha sido inicializado.");
        }
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Pool de conexiones cerrado correctamente.");
        }
    }
}