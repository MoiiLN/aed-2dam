package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
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
    public TareaRequest create(String titulo) {
        return null;
    }

    @Override
    public boolean delete(Long identificador) {
        List<TareaRequest> tareaRequests = findAll();
        boolean removed = tareaRequests.removeIf(t -> t.id() == identificador);
        if (removed) {
            try {
                saveAll(tareaRequests);
            } catch (IOException e) {
                return false;
            }
        }
        return removed;
    }

    @Override
    public List<TareaRequest> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        List<TareaRequest> tareaRequests = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = inputFormat.parse(reader)) {
            for (CSVRecord row : parser) {
                tareaRequests.add(new TareaRequest(
                        Long.parseLong(row.get("id")),
                        row.get("titulo")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tareaRequests;
    }

    @Override
    public TareaRequest findById(long identificador) {
//        return findAll().stream().filter(t -> t.id() == identificador).findFirst();
        return null;
    }

    @Override
    public TareaRequest update(TareaRequest tareaRequest) {
        List<TareaRequest> tareaRequests = findAll();
        for (int i = 0; i < tareaRequests.size(); i++) {
            if (tareaRequests.get(i).id() == tareaRequest.id()) {
                tareaRequests.set(i, tareaRequest);
                try {
                    saveAll(tareaRequests);
                } catch (IOException e) {
                    return tareaRequest;
                }
                return tareaRequest;
            }
        }
        return tareaRequest;
    }
    private void saveAll(List<TareaRequest> tareaRequests) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
            for (TareaRequest t : tareaRequests) {
                printer.printRecord(t.id(), t.titulo());
            }
        }
    }
}
