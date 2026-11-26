package aula.tareas.mapper;

import aula.tareas.api.TareaResponse;
import aula.tareas.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

public final class TareaMapperApi {
    private TareaMapperApi() {}

    public static TareaResponse toResponse(Tarea tarea) {
        return new TareaResponse(
                tarea.identificador(), tarea.nombre()
        );
    }

    public static List<TareaResponse> toResponses(List<Tarea> tareas) {
        List<TareaResponse> responses = new ArrayList<>();
        for (Tarea tarea : tareas) {
            responses.add(toResponse(tarea));
        }
        return responses;
    }
}
