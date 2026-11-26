package aula.tareas.repository;

import aula.tareas.modelo.Tarea;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryJson")
public class TareaRepositoryJson implements ITareaRepository {
    private final Path path;
    private final ObjectMapper mapper;

    public TareaRepositoryJson() {
        this(Path.of("data", "tarea.json"), new JsonMapper());
    }

    public TareaRepositoryJson(Path path, ObjectMapper mapper) {
        this.path = path;
        this.mapper = mapper;

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public synchronized List<Tarea> findAll() {
        try {
            return new ArrayList<>(mapper.readValue(
                    path.toFile(), new TypeReference<List<Tarea>>() {}));
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public synchronized Optional<Tarea> findById(long id) {
        return findAll().stream().filter(p -> p.identificador() == id).findFirst();
    }

    @Override
    public synchronized Tarea create(Tarea tarea) {
        List<Tarea> items = findAll();
        if (items.stream().anyMatch(t -> t.identificador() .equals(tarea.identificador()))) {
            throw new IllegalArgumentException("Id duplicado: " + tarea.identificador());
        }
        items.add(tarea);
        saveAll(items);
        return tarea;
    }

    @Override
    public synchronized Optional<Tarea> update(Tarea tarea) {
        List<Tarea> items = findAll();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).identificador().equals(tarea.identificador())) {
                items.set(i, tarea);
                saveAll(items);
                return Optional.of(tarea);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized boolean delete(long id) {
        List<Tarea> items = findAll();
        boolean removed = items.removeIf(t -> t.identificador() == id);
        if (removed) {
            saveAll(items);
        }
        return removed;
    }

    private void saveAll(List<Tarea> tareas) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), tareas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
