package aula.tareas.service;

import aula.tareas.api.TareaRequest;
import aula.tareas.mapper.TareaMapperApi;
import aula.tareas.mapper.TareaMapperJson;
import aula.tareas.mapper.TareaMapperServicio;
import aula.tareas.modelo.Tarea;
import aula.tareas.repository.ITareaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("tareaServiceJson")
public class TareaServiceJson implements ITareaService {
    private final ITareaRepository repository;

    public TareaServiceJson(@Qualifier("tareaRepositoryJson") ITareaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tarea> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<Tarea> buscar(long id) {
        return repository.findById(id);
    }

    @Override
    public Tarea crear(TareaRequest request) {
        return repository.create(TareaMapperServicio.toDomain(request));
    }

    @Override
    public Optional<Tarea> actualizar(long id, TareaRequest request) {
        return repository.update(TareaMapperServicio.toDomain(id, request));
    }

    @Override
    public boolean eliminar(long id) {
        return repository.delete(id);
    }
}
