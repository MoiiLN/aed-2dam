package aula.tareas.repository;

import aula.tareas.modelo.Tarea;
import org.springframework.stereotype.Repository;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository("repositoryXml")
public class TareaRepositoryXml implements ITareaRepository {
    @JacksonXmlRootElement(localName = "tareas")
    public static class DocumentoTareas {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "tarea")
        public List<Tarea> tareas = new ArrayList<>();

        public DocumentoTareas() {}
        public DocumentoTareas(List<Tarea> tareas) {
            this.tareas = new ArrayList<>(tareas);
        }
    }
    private Path path;
    private final XmlMapper mapper;
    private final AtomicLong secuencia = new AtomicLong();
    private List<Tarea> lista;
    public TareaRepositoryXml(Path path, XmlMapper mapper) {
        this.path = path;
        this.mapper = mapper;
    }

    public TareaRepositoryXml() {
        path = Path.of("data", "tarea.xml");
        mapper = new XmlMapper();
        lista = findAll();
    }

    @Override
    public Tarea create(String titulo) {
        long id = secuencia.incrementAndGet();
        var tarea = new Tarea(id, titulo.strip());
        lista.add(tarea);
        saveAll(lista);
        return tarea;
    }

    @Override
    public boolean delete(Long identificador) {
        List<Tarea> tareas = findAll();
        boolean removed = tareas.removeIf(t -> t.id() == identificador);
        if (removed) {
            saveAll(tareas);
        }
        return removed;
    }

    @Override
    public List<Tarea> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        DocumentoTareas data = mapper.readValue(path.toFile(), DocumentoTareas.class);
        return new ArrayList<>(data.tareas);
    }

    @Override
    public Tarea findById(long identificador) {
        return findAll().stream().filter(t -> t.id() == identificador).findFirst().orElse(null);
    }

    @Override
    public Tarea update(Tarea tarea) {
        List<Tarea> tareas = findAll();
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).id() == tarea.id()) {
                tareas.set(i, tarea);
                saveAll(tareas);
                return tarea;
            }
        }
        return null;
    }
    private void saveAll(List<Tarea> tareas) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), new DocumentoTareas(tareas));
        } catch (Exception e) {
            // Logger
            throw new RuntimeException("Error al utilizar SaveAll en XML", e);
        }
    }
}
