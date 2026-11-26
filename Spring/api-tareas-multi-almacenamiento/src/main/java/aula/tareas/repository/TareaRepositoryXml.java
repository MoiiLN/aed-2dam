package aula.tareas.repository;

import aula.tareas.entity.TareaEntityXml;
import aula.tareas.mapper.TareaMapperXml;
import aula.tareas.modelo.Tarea;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("tareaRepositoryXml")
public class TareaRepositoryXml extends RepositorioTareaFichero {
    private final Path path;
    private final XmlMapper mapper = XmlMapper.builder()
            .defaultUseWrapper(false)
            .build();

    public TareaRepositoryXml() {
        this(Path.of("data", "tarea.xml"));
    }

    public TareaRepositoryXml(Path path) {
        this.path = path;
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @JsonRootName("tareas")
    public static class DocumentoXml {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "tarea")
        private List<TareaEntityXml> tareas;

        public DocumentoXml() {}

        public DocumentoXml(List<TareaEntityXml> tareas) {
            this.tareas = tareas;
        }

        public List<TareaEntityXml> getTareas() {
            return tareas;
        }

        public void setTareas(List<TareaEntityXml> tareas) {
            this.tareas = tareas;
        }
    }

    @Override
    List<Tarea> readAll() {
        // TODO: Leer el documento XML y recuperar sus tareas.
        List<Tarea> tareas = new ArrayList<>();
        DocumentoXml data = mapper.readValue(path.toFile(), DocumentoXml.class);
        for (TareaEntityXml tareaEntity : data.tareas) {
            tareas.add(TareaMapperXml.toDomain(tareaEntity));
        }
        return tareas;
    }

    @Override
    void writeAll(List<Tarea> tareas) {
        // TODO: Convertir las tareas y escribir el documento XML.
        List<TareaEntityXml> tareasEntity = new ArrayList<>();
        for (Tarea tarea : tareas) {
            tareasEntity.add(TareaMapperXml.toEntity(tarea));
        }
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(path.toFile(), new DocumentoXml(tareasEntity));
    }
}
