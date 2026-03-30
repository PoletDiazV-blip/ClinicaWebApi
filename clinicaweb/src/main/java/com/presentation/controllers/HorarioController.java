package com.presentation.controllers;

import com.domain.entities.Horario;
import com.domain.repositories.IHorarioRepository;
import io.javalin.http.Context;

public class HorarioController {
    private final IHorarioRepository repository;

    public HorarioController(IHorarioRepository repository) {
        this.repository = repository;
    }

    public void crear(Context ctx) {
        Horario h = ctx.bodyAsClass(Horario.class);
        if (repository.guardar(h)) ctx.status(201).result("Horario creado");
        else ctx.status(500).result("Error al guardar");
    }

    public void listar(Context ctx) {
        // Esto aparecerá en la consola de IntelliJ (Abajo en 'Run')
        System.out.println("--- Intentando listar horarios para el doctor ID: " + ctx.pathParam("id") + " ---");

        try {
            int idDoc = Integer.parseInt(ctx.pathParam("id"));
            var lista = repository.obtenerPorDoctor(idDoc);
            System.out.println("Registros encontrados en DB: " + lista.size());
            ctx.json(lista);
        } catch (Exception e) {
            System.out.println("ERROR EN LISTAR: " + e.getMessage());
            e.printStackTrace();
            ctx.status(500).result("Error interno");
        }
    }

    public void eliminar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (repository.eliminar(id)) ctx.status(200).result("Eliminado");
        else ctx.status(500).result("Error");
    }
}
