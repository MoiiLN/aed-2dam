package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.csv.ProductoCsvRepository;
import es.codelearnacademy.filelab.json.ProductoJsonRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import es.codelearnacademy.filelab.xml.ProductoXmlRepository;

import java.nio.file.Path;

public class RepositoryFactory {

    public IProductoRepository create(FileFormat format, Path path) {
        switch (format) {
            case JSON:
                return new ProductoJsonRepository(path);
            case XML:
                return new ProductoXmlRepository(path);
            case CSV:
                return new ProductoCsvRepository(path);
            default:
                throw new IllegalArgumentException();
        }
    }
}
