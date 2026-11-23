package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PeliculaJdbcRepository implements IPeliculaRepository {

    private final String jdbcUrl;
    private final PeliculaRowMapper peliculaRowMapper;
    private final PeliculaEntityMapper peliculaEntityMapper;

    @Autowired
    public PeliculaJdbcRepository(PeliculaRowMapper peliculaRowMapper, PeliculaEntityMapper peliculaEntityMapper) {
        this("jdbc:sqlite:data/peliculas.sqlite3", peliculaRowMapper, peliculaEntityMapper);
    }

    public PeliculaJdbcRepository(String jdbcUrl, PeliculaRowMapper peliculaRowMapper, PeliculaEntityMapper peliculaEntityMapper) {
        this.jdbcUrl = jdbcUrl;
        this.peliculaRowMapper = peliculaRowMapper;
        this.peliculaEntityMapper = peliculaEntityMapper;
        if (!validarUrl(jdbcUrl)) {
            throw new RuntimeException("No se ha encontrado la bdd.");
        }

    }

    private boolean validarUrl(String jdbcUrl) {
        String url = jdbcUrl.replace("jdbc:sqlite:", "");
        Path path = Path.of(url);
        return (Files.exists(path));
    }


    @Override
    public List<Pelicula> buscarTodas() {
        String query = "SELECT pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos FROM peliculas ORDER BY pelicula_codigo";
        List<Pelicula> peliculas = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PeliculaEntity entity = peliculaRowMapper.mapRow(rs, 0);
                peliculas.add(peliculaEntityMapper.aPelicula(entity));
            }
        } catch (SQLException e) {
            return List.of();
        }
        return peliculas;

    }

    @Override
    public Optional<Pelicula> buscarPorCodigo(String identificador) {
        String query = "SELECT pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos FROM peliculas WHERE pelicula_codigo = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(query)) {
            ps.setString(1, identificador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PeliculaEntity entity = peliculaRowMapper.mapRow(rs, 0);
                    Pelicula pelicula = peliculaEntityMapper.aPelicula(entity);
                    return Optional.of(pelicula);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean insertarSiAusente(Pelicula pelicula) {
        String sql = "INSERT INTO peliculas (pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos) VALUES (?, ?, ?, ?) ON CONFLICT(pelicula_codigo) DO NOTHING";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, pelicula.getIdentificador());
            ps.setString(2, pelicula.getNombre());
            ps.setInt(3, pelicula.getAnioLanzamiento());
            ps.setInt(4, pelicula.getMinutosProyeccion());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean actualizarSiExiste(Pelicula pelicula) {
        String sql = "UPDATE peliculas SET titulo_texto = ?, estreno_anio = ?, duracion_minutos = ? WHERE pelicula_codigo = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, pelicula.getNombre());
            ps.setInt(2, pelicula.getAnioLanzamiento());
            ps.setInt(3, pelicula.getMinutosProyeccion());
            ps.setString(4, pelicula.getIdentificador());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean eliminar(String identificador) {
        String sql = "DELETE FROM peliculas WHERE pelicula_codigo = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, identificador);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}