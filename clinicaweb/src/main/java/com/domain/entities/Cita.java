package com.domain.entities;

public class Cita {
    private int id_cita;
    private int id_paciente;
    private int id_doctor;
    private String nombre_paciente;
    private String nombre_doctor;   // Agregado para que el paciente vea el nombre del Dr.
    private String motivo_cita;
    private String estado;
    private String fecha;           // ¡Súper importante para agendar!
    private String hora;            // ¡Súper importante para agendar!

    // Constructor vacío
    public Cita() {}

    // Getters y Setters
    public int getId_cita() { return id_cita; }
    public void setId_cita(int id_cita) { this.id_cita = id_cita; }

    public int getId_paciente() { return id_paciente; }
    public void setId_paciente(int id_paciente) { this.id_paciente = id_paciente; }

    public int getId_doctor() { return id_doctor; }
    public void setId_doctor(int id_doctor) { this.id_doctor = id_doctor; }

    public String getNombre_paciente() { return nombre_paciente; }
    public void setNombre_paciente(String nombre_paciente) { this.nombre_paciente = nombre_paciente; }

    public String getNombre_doctor() { return nombre_doctor; }
    public void setNombre_doctor(String nombre_doctor) { this.nombre_doctor = nombre_doctor; }

    public String getMotivo_cita() { return motivo_cita; }
    public void setMotivo_cita(String motivo_cita) { this.motivo_cita = motivo_cita; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}