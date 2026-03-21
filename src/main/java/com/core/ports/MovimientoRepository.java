package com.core.ports;

import com.core.entities.Movimiento;
import java.util.List;

public interface MovimientoRepository {
    // El "contrato" para guardar en la base de datos de AWS
    void guardar(Movimiento movimiento);

    // El "contrato" para traer los gastos de un usuario (Polet, Mari o Chepe)
    List<Movimiento> buscarPorUsuario(int idUsuario);
}