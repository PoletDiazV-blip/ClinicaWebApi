package com.core.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Movimiento {
    private Integer idMovimiento;
    private Integer idUsuario;
    private Integer idCategoria;
    private String nombreProducto;
    private BigDecimal monto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private String descripcion;

    public Movimiento(Integer idMovimiento, Integer idUsuario, Integer idCategoria, String nombreProducto, BigDecimal monto, LocalDate fecha, String descripcion) {
        this.idMovimiento = idMovimiento;
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
        this.nombreProducto = nombreProducto;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public Integer getIdMovimiento() { return idMovimiento; }
    public Integer getIdUsuario() { return idUsuario; }
    public Integer getIdCategoria() { return idCategoria; }
    public String getNombreProducto() { return nombreProducto; }
    public BigDecimal getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
}