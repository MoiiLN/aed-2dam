package aula.tareas.mapper;

import aula.tareas.api.TareaResponse;
import aula.tareas.entity.TareaEntityCsv;
import aula.tareas.modelo.Tarea;

import java.util.List;

public final class TareaMapperCsv {
    private TareaMapperCsv() {}

    public static TareaEntityCsv toEntity(Tarea tarea) {
        return new TareaEntityCsv(
                tarea.identificador(), tarea.nombre()
        );
    }

    public static Tarea toDomain(TareaEntityCsv entity) { // Added
        return new Tarea(
                entity.getIdTarea(), entity.getNombreTarea()
        );
    }
}
