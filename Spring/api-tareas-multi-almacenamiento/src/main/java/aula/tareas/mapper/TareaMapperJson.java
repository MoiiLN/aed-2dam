package aula.tareas.mapper;

import aula.tareas.entity.TareaEntityJson;
import aula.tareas.modelo.Tarea;

public final class TareaMapperJson {
    private TareaMapperJson() {}

    public static TareaEntityJson toEntity(Tarea tarea) {
        return new TareaEntityJson(tarea.identificador(), tarea.nombre());
    }

    public static Tarea toDomain(TareaEntityJson entity) {
        return new Tarea(entity.getIdTarea(), entity.getNombreTarea());
    }
}
