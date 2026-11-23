package aula.tareas.mappers;

import aula.tareas.api.TareaRequest;
import aula.tareas.entity.TareaEntityJdbc;
import aula.tareas.modelo.Tarea;

public class TareaMapperJdbc {

    static public TareaRequest toDomain(TareaEntityJdbc entityJdbc) {
        return null;
    }

    static public TareaEntityJdbc toJdbc(Tarea tarea) {
        if (tarea == null) {
            return null;
        }
        return null;
    }
}
