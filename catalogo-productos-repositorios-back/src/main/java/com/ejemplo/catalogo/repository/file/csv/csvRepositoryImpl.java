package com.ejemplo.catalogo.repository.file.csv;

import com.ejemplo.catalogo.model.Producto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class csvRepositoryImpl extends AbstractRepositoryMoi implements RepositoryInterface {
    private final CSVFormat inputFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("id", "nombre", "precio")
            .get();
    csvRepositoryImpl(Path path) {
        super(path);
        productos = load();
    }


    @Override
    public void create(Producto producto) {
        if (producto == null || producto.id() < 0) {
            return;
        }
        List<Producto> productos = findAll();
        if (productos.stream().anyMatch(p -> p.id() == producto.id()))
            throw new IllegalArgumentException("Id duplicado: " + producto.id());
        productos.add(producto);
        saveAll(productos);
    }

    @Override
    public boolean update(Producto producto) {
        if (producto == null || producto.id() < 0) {
            return false;
        }
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).id() == producto.id()) {
                productos.set(i, producto);
                saveAll(productos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        boolean removed = productos.removeIf(p -> p.id() == id);
        if (removed) {
            saveAll(productos);
        }
        return removed;
    }
    @Override
    public List<Producto> load() {
        try (Reader reader = Files.newBufferedReader(getPath(), StandardCharsets.UTF_8);
             CSVParser parser = inputFormat.parse(reader)) {
            for (CSVRecord row : parser) {
                productos.add(new Producto(
                        Long.parseLong(row.get("id")),
                        row.get("nombre"),
                        Double.parseDouble(row.get("precio"))));
            }
        } catch (IOException e) {
            // Logger Error / Fine
        }
        return productos;
    }
    @Override
    private void saveAll(List<Producto> productos) {
            try (Writer writer = Files.newBufferedWriter(getPath(), StandardCharsets.UTF_8);
                 CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
                for (Producto p : productos) printer.printRecord(p.id(), p.nombre(), p.precio());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
}
