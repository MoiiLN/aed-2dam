package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import aula.tareas.modelo.Tarea;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryCsv")
public class TareaRepositoryCsv implements ITareaRepository {
    private final Path path;

    public TareaRepositoryCsv() {
        this(Path.of("data", "tarea.csv"));
    }

    public TareaRepositoryCsv(Path path) {
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


    private static final CSVFormat inputFormat = CSVFormat.DEFAULT.builder()
            .setHeader().setSkipHeaderRecord(true).get();
    private static final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("id", "titulo").get();

    @Override
    public synchronized List<Tarea> findAll() {
        List<Tarea> tareas = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = inputFormat.parse(reader)) {
            for (CSVRecord row : parser) {
                tareas.add(new Tarea(
                        Long.parseLong(row.get("id")),
                        row.get("titulo")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tareas;
    }

    @Override
    public synchronized Optional<Tarea> findById(long id) {
        return findAll().stream().filter(p -> p.identificador() == id).findFirst();
    }

    @Override
    public synchronized Tarea create(Tarea tarea) {
        List<Tarea> tareas = findAll();
        if (tareas.stream().anyMatch(p -> p.identificador().equals(tarea.identificador()))) {
            throw new IllegalArgumentException("Id duplicado: " + tarea.identificador());
        }
        tareas.add(tarea);
        saveAll(tareas);
        return tarea;
    }

    @Override
    public synchronized Optional<Tarea> update(Tarea tarea) {
        List<Tarea> tareas = findAll();
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).identificador().equals(tarea.identificador())) {
                tareas.set(i, tarea);
                saveAll(tareas);
                return Optional.of(tarea);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized boolean delete(long id) {
        List<Tarea> tareas = findAll();
        boolean removed = tareas.removeIf(p -> p.identificador() == id);
        if (removed) {
            saveAll(tareas);
        }
        return removed;
    }

    private void saveAll(List<Tarea> tareas) {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
            for (Tarea t : tareas) {
                printer.printRecord(t.identificador(), t.nombre());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
