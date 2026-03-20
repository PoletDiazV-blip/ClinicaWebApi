package com.presentation.dtos;

/**
 * Este es un 'record' DTO.
 * Solo sirve para recibir el JSON que mandamos desde el cliente.
 */
public record LoginRequest(String username, String password) {}