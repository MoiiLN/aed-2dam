package aula.tareas.mapper;

import aula.tareas.api.TareaRequest;
import aula.tareas.modelo.Tarea;

public final class TareaMapperServicio {
    private TareaMapperServicio() {}

    public static Tarea toDomain(TareaRequest request) {
        return new Tarea(request.id(), request.titulo());
    }

    public static Tarea toDomain(long id, TareaRequest request) {
        return new Tarea(id, request.titulo());
    }
}
