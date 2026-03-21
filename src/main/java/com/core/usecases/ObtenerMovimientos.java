package com.core.usecases;

import com.core.entities.Movimiento;
import com.data.repositories.MovimientoRepositoryImpl;
import java.util.List;

public class ObtenerMovimientos {
    private final MovimientoRepositoryImpl repositorio;

    public ObtenerMovimientos(MovimientoRepositoryImpl repositorio) {
        this.repositorio = repositorio;
    }

    public List<Movimiento> ejecutar(int idUsuario) {
        return this.repositorio.buscarPorUsuario(idUsuario);
    }
}