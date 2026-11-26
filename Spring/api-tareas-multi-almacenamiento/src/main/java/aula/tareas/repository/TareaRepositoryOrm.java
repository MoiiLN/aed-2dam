package aula.tareas.repository;

import aula.tareas.entity.TareaEntityOrm;
import aula.tareas.mapper.TareaMapperOrm;
import aula.tareas.modelo.Tarea;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryOrm")
public class TareaRepositoryOrm implements ITareaRepository {
    private final ITareaRepositoryJpa jpa;

    public TareaRepositoryOrm(ITareaRepositoryJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Tarea> findAll() {
        // TODO: Recuperar las tareas de este almacenamiento.
        List<Tarea> tareas = new ArrayList<>();
        List<TareaEntityOrm> tareasOrm = jpa.findAll();
        for (TareaEntityOrm tareaOrm : tareasOrm) {
            tareas.add(TareaMapperOrm.toDomain(tareaOrm));
        }
        return tareas;
    }

    @Override
    public Optional<Tarea> findById(long id) {
        // TODO: Buscar una tarea por identificador.
        Optional<TareaEntityOrm> tareaOrm = jpa.findById(id);
        if (tareaOrm.isPresent()) {
            return Optional.of(TareaMapperOrm.toDomain(tareaOrm.get()));
        }
        return Optional.empty();
    }

    @Override
    public Tarea create(Tarea tarea) {
        // TODO: Guardar la tarea nueva y devolverla con su identificador.
        TareaEntityOrm tareaOrm = TareaMapperOrm.toEntity(tarea);
        TareaEntityOrm tareaCreted = jpa.save(tareaOrm);
        return null;
        //
    }

    @Override
    public Optional<Tarea> update(Tarea tarea) {
        // TODO: Actualizar una tarea existente.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public boolean delete(long id) {
        // TODO: Eliminar la tarea y comunicar si existía.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
