package com.domain.repositories;

import com.domain.entities.Horario;
import java.util.List;

public interface IHorarioRepository {
    boolean guardar(Horario horario);
    List<Horario> obtenerPorDoctor(int id_doctor);
    boolean eliminar(int id_horario);
}
