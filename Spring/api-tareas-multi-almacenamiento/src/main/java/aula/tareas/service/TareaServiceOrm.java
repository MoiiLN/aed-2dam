package aula.tareas.service;

import aula.tareas.api.TareaRequest;
import aula.tareas.mapper.TareaMapperServicio;
import aula.tareas.modelo.Tarea;
import aula.tareas.repository.ITareaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("tareaServiceOrm")
public class TareaServiceOrm implements ITareaService {
    private final ITareaRepository repository;

    public TareaServiceOrm(@Qualifier("tareaRepositoryOrm") ITareaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tarea> listar() {
        // TODO: Recuperar el listado mediante el repositorio.
        return repository.findAll();
    }

    @Override
    public Optional<Tarea> buscar(long id) {
        // TODO: Consultar el repositorio con el identificador.
        return repository.findById(id);
    }

    @Override
    public Tarea crear(TareaRequest request) {
        // TODO: Convertir la petición a dominio y solicitar el alta.
        Tarea tarea = TareaMapperServicio.toDomain(request);
        return repository.create(tarea);
    }

    @Override
    public Optional<Tarea> actualizar(long id, TareaRequest request) {
        // TODO: Convertir la petición usando el ID y solicitar el cambio.
        Tarea tarea = TareaMapperServicio.toDomain(id, request);
        return repository.update(tarea);
    }

    @Override
    public boolean eliminar(long id) {
        // TODO: Solicitar el borrado al repositorio.
        return repository.delete(id);
    }
}
