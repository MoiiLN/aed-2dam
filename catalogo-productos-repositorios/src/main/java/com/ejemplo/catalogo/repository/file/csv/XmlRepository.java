package com.ejemplo.catalogo.repository.file.csv;

import com.ejemplo.catalogo.model.Producto;
import com.ejemplo.catalogo.model.ProductosXml;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class XmlRepository extends AbstractRepository {
    private final XmlMapper mapper;
    public XmlRepository(Path path) {
        super(path);
        productos = load();
        mapper = new XmlMapper();
    }

    @Override
    public void saveAll(List<Producto> productos) {
        Path temporal = null;
        try {
            ProductosXml productosXml = new ProductosXml();
            ProductosXml.setProductos();
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(getPath().toFile(), productosXml);
            Path destino = getPath().toAbsolutePath();
            Path directorio = destino.getParent();
            Files.createDirectories(directorio);
            temporal = Files.createTempFile(directorio, "productos-", ".json.tmp");
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporal.toFile(), productos);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar " + getPath(), e);
        } finally {
            if (temporal != null) {
                try { Files.deleteIfExists(temporal); } catch (IOException ignored) { }
            }
        }
    }

    @Override
    public List<Producto> load() {
        try {
            ProductosXml productosXml = mapper.readValue(getPath().toFile(), ProductosXml.class);
            productos.clear();
            productos.addAll(productosXMl.getProductos());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + getPath(), e);
        }
        return productos;
    }
}
