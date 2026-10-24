
package es.ejemplo.taller.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {

    private ConexionBD() {}

    public static String normalizarUrl(String url) throws SQLException {

        if (url == null || !url.startsWith("jdbc:sqlite:")) {
            throw new SQLException("URL no válida");
        }

        String archivo = url.substring("jdbc:sqlite:".length());

        if (archivo.isBlank() || archivo.equals(":memory:")
                || archivo.startsWith("file:")) {
            throw new SQLException("Ruta no válida");
        }

        try {
            Path ruta = Paths.get(archivo).toAbsolutePath().normalize();

            if (!Files.exists(ruta) || !Files.isRegularFile(ruta)
                    || !Files.isReadable(ruta)) {
                throw new SQLException("El archivo no existe o no se puede leer");
            }

            return "jdbc:sqlite:" + ruta;

        } catch (SQLException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new SQLException("La ruta no es válida", e);
        }
    }

    public static Connection abrir(String url) throws SQLException {
        return DriverManager.getConnection(normalizarUrl(url));
    }
}
