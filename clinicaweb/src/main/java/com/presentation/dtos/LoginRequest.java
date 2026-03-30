package com.presentation.dtos;

public class LoginRequest {
    public String correo;
    public String password;

    public LoginRequest() {}

    // Método para validar que no manden campos vacíos
    public boolean isInvalid() {
        return correo == null || correo.isBlank() || password == null || password.isBlank();
    }
}