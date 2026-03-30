package com.data.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    // Logger para imprimir mensajes profesionales en la consola
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;

    static {
        try {
            logger.info("Iniciando configuracion del pool de conexiones HikariCP...");

            HikariConfig config = new HikariConfig();

            // Configuración principal con tus credenciales
            config.setJdbcUrl("jdbc:mysql://localhost:3306/clinica_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
            config.setUsername("root");
            config.setPassword("albafica");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // Blindaje y Optimización (Timeout y Pool)
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            // Tiempo máximo de espera para obtener una conexión (30 segundos)
            config.setConnectionTimeout(30000);
            // Tiempo máximo que una conexión puede estar inactiva
            config.setIdleTimeout(600000);
            // Máximo de conexiones simultáneas a tu Workbench
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);

            // Prueba de fuego: Intentamos conectar de una vez para validar
            dataSource.getConnection().close();
            logger.info("Conexion exitosa a la base de datos: clinica_db");

        } catch (SQLException e) {
            logger.error("ERROR CRÍTICO: No se pudo conectar a MySQL Workbench.");
            logger.error("Detalle del error: {}", e.getMessage());
            logger.error("Causas probables: ¿MySQL está encendido? ¿La contraseña 'albafica' es correcta?");
            throw new RuntimeException("Fallo al inicializar la base de datos", e);
        } catch (Exception e) {
            logger.error("ERROR INESPERADO al configurar HikariCP: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene una conexión del pool.
     * @return Connection lista para usar.
     * @throws SQLException si el pool no puede entregar una conexión.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("El DataSource no ha sido inicializado.");
        }
        return dataSource.getConnection();
    }

    // Método para cerrar el pool cuando la app se apague (opcional pero recomendado)
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Pool de conexiones cerrado correctamente.");
        }
    }
}