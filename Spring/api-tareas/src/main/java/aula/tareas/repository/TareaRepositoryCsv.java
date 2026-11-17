package aula.tareas.repository;

import aula.tareas.modelo.Tarea;
import org.apache.commons.csv.CSVFormat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TareaRepositoryCsv implements ITareaRepository{

    private Path path;
    private CSVFormat inputFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build(); // get
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("id", "titulo")
            .build(); // get
    public TareaRepositoryCsv() {
        path = Path.of("data", "tarea.csv");
    }
    @Override
    public Tarea create(String titulo) {
        return null;
    }

    @Override
    public boolean delete(Long identificador) {
        return false;
    }

    @Override
    public List<Tarea> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
    }

    @Override
    public Tarea findById(long identificador) {
        return null;
    }

    @Override
    public Tarea update(Tarea tarea) {
        return null;
    }
}
