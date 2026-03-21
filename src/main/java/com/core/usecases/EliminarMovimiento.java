package com.core.usecases;

import com.data.repositories.MovimientoRepositoryImpl;

public class EliminarMovimiento {
    private final MovimientoRepositoryImpl repositorio;

    public EliminarMovimiento(MovimientoRepositoryImpl repositorio) {
        this.repositorio = repositorio;
    }

    public void ejecutar(int idMovimiento) {
        this.repositorio.eliminar(idMovimiento);
    }
}