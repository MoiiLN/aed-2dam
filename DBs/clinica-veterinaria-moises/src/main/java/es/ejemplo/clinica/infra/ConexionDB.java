
package es.ejemplo.clinica.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class ConexionDB {

    private ConexionDB() {}

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

//package es.ejemplo.clinica.infra;
//
//import es.ejemplo.clinica.dao.DaoException;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public final class ConexionDB {
//
//    private ConexionDB() {}
//
//    public static String normalizarUrl(String url) throws SQLException {
//        if (url == null || !url.startsWith("jdbc:sqlite:")) {
//            throw new SQLException("URL no válida");
//        }
//
//        String archivo = url.substring("jdbc:sqlite:".length());
//
//        if (archivo.isBlank()
//                || archivo.equals(":memory:")
//                || archivo.startsWith("file:")) {
//            throw new SQLException("Ruta no válida");
//        }
//
//        try {
//            Path ruta = Paths.get(archivo).toAbsolutePath().normalize();
//
//            if (Files.exists(ruta)) {
//                if (!Files.isRegularFile(ruta)
//                        || !Files.isReadable(ruta)) {
//                    throw new SQLException(
//                            "El archivo no es válido o no se puede leer"
//                    );
//                }
//            }
//
//            return "jdbc:sqlite:" + ruta;
//
//        } catch (SQLException e) {
//            throw e;
//        } catch (RuntimeException e) {
//            throw new SQLException("La ruta no es válida", e);
//        }
//    }
//
//    public static Connection abrir(String url) throws SQLException {
//        String urlNormalizada = normalizarUrl(url);
//        String archivo = urlNormalizada.substring(
//                "jdbc:sqlite:".length()
//        );
//
//        Path ruta = Paths.get(archivo);
//        Path directorio = ruta.getParent();
//
//        try {
//            if (directorio != null) {
//                Files.createDirectories(directorio);
//            }
//        } catch (IOException | RuntimeException e) {
//            throw new SQLException(
//                    "No se pudieron crear las carpetas de la base de datos",
//                    e
//            );
//        }
//
//        Connection conn = DriverManager.getConnection(urlNormalizada);
//
//        try {
//            try (Statement st = conn.createStatement()) {
//                st.execute("PRAGMA foreign_keys = ON");
//            }
//
//            return conn;
//
//        } catch (SQLException e) {
//            try {
//                conn.close();
//            } catch (SQLException cierre) {
//                e.addSuppressed(cierre);
//            }
//
//            throw new DaoException(
//                    "No se pudieron activar las claves foráneas",
//                    e
//            );
//        }
//    }
//}
