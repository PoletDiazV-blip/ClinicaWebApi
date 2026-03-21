package com.presentation.controllers;

import com.core.entities.Movimiento;
import com.core.usecases.*;
import com.presentation.dtos.MovimientoRequest;
import io.javalin.http.Context;
import java.time.LocalDate;

public class MovimientoController {
    private final RegistrarMovimiento registrarMovimiento;
    private final ObtenerMovimientos obtenerMovimientos;
    private final EliminarMovimiento eliminarMovimiento;

    // Constructor con las 3 herramientas
    public MovimientoController(RegistrarMovimiento reg, ObtenerMovimientos obt, EliminarMovimiento eli) {
        this.registrarMovimiento = reg;
        this.obtenerMovimientos = obt;
        this.eliminarMovimiento = eli;
    }

    public void crear(Context ctx) {
        MovimientoRequest req = ctx.bodyAsClass(MovimientoRequest.class);
        Movimiento m = new Movimiento(null, req.idUsuario, req.idCategoria, req.nombreProducto, req.monto, LocalDate.parse(req.fecha), req.descripcion);
        registrarMovimiento.ejecutar(m);
        ctx.status(201).result("Registrado con exito");
    }

    public void obtenerPorUsuario(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(obtenerMovimientos.ejecutar(id));
    }

    public void eliminar(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            eliminarMovimiento.ejecutar(id);

            // CAMBIO AQUÍ: Usamos .status(200) y mandamos el mensaje que quieres
            ctx.status(200).result("Eliminación exitosa! El registro " + id + " ya no existe");

        } catch (Exception e) {
            ctx.status(500).result("Error al intentar eliminar: " + e.getMessage());
        }
    }
}