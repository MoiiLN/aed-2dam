package es.codelearnacademy.filelab.csv;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.model.Vehiculo;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IVehiculoRepository;
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

public class VehiculoCsvRepository
        extends AbstractFileRepository<Vehiculo, String>
        implements IVehiculoRepository {

    private final Path path;

    public VehiculoCsvRepository(Path path) {
        this.path = path;
    }
    private Vehiculo toVehiculo(CSVRecord record) {
        return new Vehiculo(
                record.get("matricula"),
                record.get("marca"),
                record.get("modelo"),
                Integer.parseInt(record.get("anio"))
        );
    }
    private Object[] toCsv(Vehiculo vehiculo) {
        return new Object[]{vehiculo.matricula(), vehiculo.marca(), vehiculo.modelo(), vehiculo.anio()};
    }
    @Override
    protected String getId(Vehiculo vehiculo) {
        return vehiculo.matricula();
    }

    @Override
    protected List<Vehiculo> readAll() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .get();
        List<Vehiculo> vehiculos = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = format.parse(reader)) {
            for (CSVRecord record : parser) {
                vehiculos.add(toVehiculo(record));
            }
        }
        return vehiculos;
    }

    @Override
    protected void writeAll(List<Vehiculo> vehiculos) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("id", "nombre", "precio", "stock")
                .get();
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, format)) {
            for (Vehiculo vehiculo : vehiculos) {
                printer.printRecord(toCsv(vehiculo));
            }
        }
    }
}
