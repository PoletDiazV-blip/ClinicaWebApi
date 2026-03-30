package com.domain.entities;

/**
 * Entidad Doctor: Centraliza TODA la información para Perfil e Información Profesional.
 * Incluye campos de ambas pantallas para evitar errores de mapeo con Jackson.
 */
public class Doctor {
    // Identificadores
    private int id_doctor;
    private int id_usuario;

    // Datos Personales (Para Perfil)
    private String nombre;
    private int edad;      // <-- AGREGADO PARA PERFIL
    private String sexo;   // <-- AGREGADO PARA PERFIL

    // Datos Profesionales (Para Información Profesional)
    private String especialidad;
    private String cedula;
    private String telefono;
    private String correo;
    private String biografia;

    // 1. CONSTRUCTOR VACÍO (Indispensable para Javalin/Jackson)
    public Doctor() {
    }

    // 2. CONSTRUCTOR COMPLETO (Para pruebas o instanciación rápida)
    public Doctor(int id_doctor, int id_usuario, String nombre, int edad, String sexo,
                  String especialidad, String cedula, String telefono, String correo, String biografia) {
        this.id_doctor = id_doctor;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.especialidad = especialidad;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.biografia = biografia;
    }

    // 3. GETTERS Y SETTERS (Todos los cables conectados)

    public int getId_doctor() { return id_doctor; }
    public void setId_doctor(int id_doctor) { this.id_doctor = id_doctor; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
}