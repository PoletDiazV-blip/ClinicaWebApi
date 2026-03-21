package com.core.usecases;

import com.core.entities.Movimiento;
import com.data.repositories.MovimientoRepositoryImpl;

public class RegistrarMovimiento {
    private final MovimientoRepositoryImpl repositorio;

    public RegistrarMovimiento(MovimientoRepositoryImpl repositorio) {
        this.repositorio = repositorio;
    }

    public void ejecutar(Movimiento m) {
        this.repositorio.guardar(m);
    }
}