package aula.tareas.repository;

import aula.tareas.modelo.Tarea;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository("repositoryCsv")
public class TareaRepositoryCsv implements ITareaRepository{
    private final AtomicLong secuencia = new AtomicLong();
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
        List<Tarea> tareas = findAll();
        boolean removed = tareas.removeIf(t -> t.id() == identificador);
        if (removed) {
            try {
                saveAll(tareas);
            } catch (IOException e) {
                return false;
            }
        }
        return removed;
    }

    @Override
    public List<Tarea> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
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
    public Tarea findById(long identificador) {
//        return findAll().stream().filter(t -> t.id() == identificador).findFirst();
        return null;
    }

    @Override
    public Tarea update(Tarea tarea) {
        List<Tarea> tareas = findAll();
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).id() == tarea.id()) {
                tareas.set(i, tarea);
                try {
                    saveAll(tareas);
                } catch (IOException e) {
                    return tarea;
                }
                return tarea;
            }
        }
        return tarea;
    }
    private void saveAll(List<Tarea> tareas) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
            for (Tarea t : tareas) {
                printer.printRecord(t.id(), t.titulo());
            }
        }
    }
}
