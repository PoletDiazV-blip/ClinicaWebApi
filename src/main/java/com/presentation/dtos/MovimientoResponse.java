package com.presentation.dtos;

import java.math.BigDecimal;

public class MovimientoResponse {
    public Integer idUsuario;
    public String nombreProducto;
    public BigDecimal monto;
    public String fecha;

    public MovimientoResponse(Integer idUsuario, String nombreProducto, BigDecimal monto, String fecha) {
        this.idUsuario = idUsuario;
        this.nombreProducto = nombreProducto;
        this.monto = monto;
        this.fecha = fecha;
    }
}