package es.codelearnacademy.filelab.csv;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
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
import java.util.ArrayList;
import java.util.List;

public class ProductoCsvRepository
        extends AbstractFileRepository<Producto, Long>
        implements IProductoRepository {

    private final Path path;

    public ProductoCsvRepository(Path path) {
        this.path = path;
    }

    private Producto toProducto(CSVRecord record) {
        return new Producto(
                Long.parseLong(record.get("id")),
                record.get("nombre"),
                Double.parseDouble(record.get("precio")),
                Integer.parseInt(record.get("stock"))
        );
    }
    private Object[] toCsv(Producto producto) {
        return new Object[]{producto.id(), producto.nombre(), producto.precio(), producto.stock()};
    }
    @Override
    protected Long getId(Producto producto) {
        return producto.id();
    }

    @Override
    protected List<Producto> readAll() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .get();
        List<Producto> productos = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = format.parse(reader)) {
            for (CSVRecord record : parser) {
                productos.add(toProducto(record));
            }
        }
        return productos;
    }

    @Override
    protected void writeAll(List<Producto> productos) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("id", "nombre", "precio", "stock")
                .get();
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, format)) {
            for (Producto producto : productos) {
                printer.printRecord(toCsv(producto));
            }
        }
    }
}