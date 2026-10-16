package es.ejemplo.gestion.config;

import es.ejemplo.gestion.exception.AplicationException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

/** Abre una conexión en la base SQLite indicada por el constructor. */
public class ConexionBD {
    private  String url;

    public ConexionBD(String url) {
        this.url = url;
        Path path = Path.of(url);
    }

    public Connection obtenerConexion() {
        try (Connection connection = DriverManager.getConnection(url)) {
            return connection;
        } catch (SQLException e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
    }
}
