package aula.tareas.mapper;

import aula.tareas.api.TareaResponse;
import aula.tareas.entity.TareaEntityXml;
import aula.tareas.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

public final class TareaMapperXml {
    private TareaMapperXml() {}

    public static TareaEntityXml toEntity(Tarea tarea) {
        return new TareaEntityXml(tarea.identificador(), tarea.nombre());
    }

    public static Tarea toDomain(TareaEntityXml entity) {
        return new Tarea(entity.getIdTarea(), entity.getNombreTarea());
    }
}
