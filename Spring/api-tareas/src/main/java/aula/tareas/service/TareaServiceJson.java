package aula.tareas.service;

import aula.tareas.modelo.Tarea;
import aula.tareas.repository.ITareaRepository;
import aula.tareas.repository.TareaRepositoryJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service("tareaServiceJson")
public class TareaServiceJson implements ITareaService {

    private final ITareaRepository tareaRepositoryJson;

    public TareaServiceJson(@Qualifier("repositoryJson") ITareaRepository tareaRepositoryJson) {
        this.tareaRepositoryJson = tareaRepositoryJson;
    }

    @Override
    public List<Tarea> listar() {
        return tareaRepositoryJson.findAll();
    }

    @Override
    public Tarea buscar(long id) {
        return tareaRepositoryJson.findById(id);
    }

    @Override
    public Tarea crear(String titulo) {
        return tareaRepositoryJson.create(titulo);
    }
}
