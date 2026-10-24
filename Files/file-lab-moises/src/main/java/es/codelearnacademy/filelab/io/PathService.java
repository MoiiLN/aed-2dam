package es.codelearnacademy.filelab.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathService {

    public Path crear(String primero, String... partes) {
        Path path = Path.of(primero, partes);
        return path;
    }

    public String nombre(Path path) {
        if (path == null) {
            return "";
        }
        return path.getFileName().toString();
    }

    public Path padre(Path path) {
        return path.getParent();
    }

    public Path absoluto(Path path) {
        return path.toAbsolutePath();
    }

    public Path normalizar(Path path) {
        return path.normalize();
    }

    public boolean esAbsoluto(Path path) {
        return path.isAbsolute();
    }

    public Path resolver(Path base, String otro) {
        return base.resolve(otro);
    }

    public Path relativizar(Path base, Path destino) {
        return base.relativize(destino);
    }

    public String extension(Path path) {
        String file_name = path.getFileName().toString();
        int position = file_name.lastIndexOf(".");
        if (position == -1) {
            return "";
        }
        return file_name.substring(position + 1);
    }
}
