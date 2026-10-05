package es.codelearnacademy.filelab.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

public class FilesService {

    public boolean existe(Path path) {
        return Files.exists(path);
    }

    public Optional<Path> crearDirectorio(Path path) {
        try {
            Files.createDirectory(path);
            return Optional.of(path);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<Path> crearDirectorios(Path path) {
        try {
            Files.createDirectories(path);
            return Optional.of(path);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<Path> crearArchivo(Path path) {
        try {
            Files.createFile(path);
            return Optional.of(path);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<Path> copiar(Path origen, Path destino) {
        try {
            Files.copy(origen, destino);
            return Optional.of(destino);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<Path> mover(Path origen, Path destino) {
        try {
            Files.move(origen, destino);
            return Optional.of(destino);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean eliminar(Path path) {
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public OptionalLong tamanio(Path path) {
        try {
            long tamanio = Files.size(path);
            return OptionalLong.of(tamanio);

        } catch (IOException e) {
            return OptionalLong.empty();
        }
    }
}
