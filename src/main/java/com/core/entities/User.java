package com.core.entities;

public record User(
        int id,
        String username,
        String passwordHash,
        String nombreCompleto,
        int idRol,
        double presupuesto
) {}