package com.domain.entities;

/**
 * Esta clase es una Entidad de Dominio.
 * Es "pura", lo que significa que no sabe nada de SQL ni de la base de datos.
 */
public class Usuario {
    private int id;
    private String correo;
    private String password; // Aquí se guardará el Hash de BCrypt
    private String rol;

    // 1. CONSTRUCTOR VACÍO (Indispensable para que el Backend lea el JSON de Postman)
    public Usuario() {}

    // 2. CONSTRUCTOR COMPLETO
    public Usuario(int id, String correo, String password, String rol) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }

    // --- SETTERS (Ahora con setId para que el CRUD funcione al 100) ---
    public void setId(int id) { this.id = id; } // <--- ELIMINA EL ERROR DEL CONTROLLER
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }
}