package aula.tareas.service;

import aula.tareas.modelo.Tarea;
import aula.tareas.repository.ITareaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@Service("tareaServiceXml")
public class TareaServiceXml implements ITareaService {
    private final AtomicLong secuencia = new AtomicLong();
    private final ConcurrentSkipListMap<Long, Tarea> tareas = new ConcurrentSkipListMap<>();
    private final ITareaRepository repository;

    public TareaServiceXml(@Qualifier("repositoryXml") ITareaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tarea> listar() {
        return repository.findAll();
    }

    @Override
    public Tarea buscar(long id) {
        return repository.findById(id);
    }

    @Override
    public Tarea crear(String titulo) {
        return repository.create(titulo);
    }
}
