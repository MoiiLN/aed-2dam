package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
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
        public List<TareaRequest> tareaRequests = new ArrayList<>();

        public DocumentoTareas() {}
        public DocumentoTareas(List<TareaRequest> tareaRequests) {
            this.tareaRequests = new ArrayList<>(tareaRequests);
        }
    }
    private Path path;
    private final XmlMapper mapper;
    private final AtomicLong secuencia = new AtomicLong();
    private List<TareaRequest> lista;
    public TareaRepositoryXml(Path path, XmlMapper mapper) {
        this.path = path;
        this.mapper = mapper;
    }

    public TareaRepositoryXml() {
        path = Path.of("data", "tarea.xml");
        mapper = new XmlMapper();
    }

    @Override
    public TareaRequest create(String titulo) {
        long id = secuencia.incrementAndGet();
        var tarea = new TareaRequest(id, titulo.strip());
        lista.add(tarea);
        saveAll(lista);
        return tarea;
    }

    @Override
    public boolean delete(Long identificador) {
        List<TareaRequest> tareaRequests = findAll();
        boolean removed = tareaRequests.removeIf(t -> t.id() == identificador);
        if (removed) {
            saveAll(tareaRequests);
        }
        return removed;
    }

    @Override
    public List<TareaRequest> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        DocumentoTareas data = mapper.readValue(path.toFile(), DocumentoTareas.class);
        return new ArrayList<>(data.tareaRequests);
    }

    @Override
    public TareaRequest findById(long identificador) {
        return findAll().stream().filter(t -> t.id() == identificador).findFirst().orElse(null);
    }

    @Override
    public TareaRequest update(TareaRequest tareaRequest) {
        List<TareaRequest> tareaRequests = findAll();
        for (int i = 0; i < tareaRequests.size(); i++) {
            if (tareaRequests.get(i).id() == tareaRequest.id()) {
                tareaRequests.set(i, tareaRequest);
                saveAll(tareaRequests);
                return tareaRequest;
            }
        }
        return null;
    }
    private void saveAll(List<TareaRequest> tareaRequests) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), new DocumentoTareas(tareaRequests));
        } catch (Exception e) {
            // Logger
            throw new RuntimeException("Error al utilizar SaveAll en XML", e);
        }
    }
}
