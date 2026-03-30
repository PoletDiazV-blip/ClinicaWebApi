package com.domain.entities;

public class Horario {
    private int id_horario;
    private int id_doctor;
    private String dia_semana;
    private String hora_inicio;
    private String hora_fin;
    private boolean disponible;

    public Horario() {}

    // Constructor para cuando el doctor crea uno nuevo
    public Horario(int id_doctor, String dia_semana, String hora_inicio, String hora_fin) {
        this.id_doctor = id_doctor;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.disponible = true;
    }

    // Getters y Setters (Asegúrate de tenerlos todos para que Javalin pueda leerlos)
    public int getId_horario() { return id_horario; }
    public void setId_horario(int id_horario) { this.id_horario = id_horario; }
    public int getId_doctor() { return id_doctor; }
    public void setId_doctor(int id_doctor) { this.id_doctor = id_doctor; }
    public String getDia_semana() { return dia_semana; }
    public void setDia_semana(String dia_semana) { this.dia_semana = dia_semana; }
    public String getHora_inicio() { return hora_inicio; }
    public void setHora_inicio(String hora_inicio) { this.hora_inicio = hora_inicio; }
    public String getHora_fin() { return hora_fin; }
    public void setHora_fin(String hora_fin) { this.hora_fin = hora_fin; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
