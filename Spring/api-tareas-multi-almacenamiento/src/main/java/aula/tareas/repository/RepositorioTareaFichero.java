package aula.tareas.repository;

import aula.tareas.modelo.Tarea;

import java.util.List;
import java.util.Optional;

/** CRUD compartido por los formatos de fichero, con lectura del fichero en cada operación. */
abstract class RepositorioTareaFichero implements ITareaRepository {
    abstract List<Tarea> readAll();
    abstract void writeAll(List<Tarea> tareas);

    @Override
    public synchronized List<Tarea> findAll() {
        // TODO: Leer el documento XML y recuperar sus tareas.
        return readAll();
    }

    @Override
    public synchronized Optional<Tarea> findById(long id) {
        // TODO: Buscar una tarea por identificador.
        List<Tarea> tareas = findAll();
        for (Tarea tarea : tareas) {
            if (tarea.identificador().equals(id)) {
                return Optional.of(tarea);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized Tarea create(Tarea tarea) {
        // TODO: Guardar la tarea nueva y devolverla con su identificador.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public synchronized Optional<Tarea> update(Tarea tarea) {
        // TODO: Actualizar una tarea existente.
        List<Tarea> tareas = findAll();
        Optional<Tarea> result = findById(tarea.identificador());
        if (result.isPresent()) {
            for (int i = 0; i < tareas.size(); i++) {
                if (tareas.get(i).identificador().equals(tarea.identificador())) {
                    tareas.set(i, tarea);
                    writeAll(tareas);
                    return Optional.of(tarea);
                }
            }
        }
        return result;
    }

    @Override
    public synchronized boolean delete(long id) {
        List<Tarea> tareas = findAll();
        // TODO: Eliminar la tarea y comunicar si existía.
        boolean deleted = tareas.removeIf(t -> t.identificador() == id);
        if (deleted) {
            writeAll(tareas);
        }
        return deleted;
    }
}
