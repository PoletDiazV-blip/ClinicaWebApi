package com.presentation.dtos;

import java.math.BigDecimal;

public class MovimientoRequest {
    public Integer idUsuario;
    public Integer idCategoria;
    public String nombreProducto;
    public BigDecimal monto;
    public String fecha; // Llega como texto "2026-03-20"
    public String descripcion;
}