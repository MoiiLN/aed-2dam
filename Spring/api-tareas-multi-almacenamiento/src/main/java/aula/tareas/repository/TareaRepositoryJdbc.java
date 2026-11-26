package aula.tareas.repository;

import aula.tareas.entity.TareaEntityJdbc;
import aula.tareas.mapper.TareaMapperJdbc;
import aula.tareas.mapper.TareaRowMapperJdbc;
import aula.tareas.modelo.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryJdbc")
public class TareaRepositoryJdbc implements ITareaRepository {
    private final JdbcTemplate jdbc;
    private final String jdbcUrl;
    private final TareaRowMapperJdbc tareaRowMapperJdbc;
    private final TareaMapperJdbc tareaMapperJdbc;

    @Autowired
    public TareaRepositoryJdbc(JdbcTemplate jdbc, TareaRowMapperJdbc tareaRowMapperJdbc, TareaMapperJdbc tareaMapperJdbc) {
        this(jdbc, "jdbc:sqlite:data/peliculas.sqlite3", tareaRowMapperJdbc, tareaMapperJdbc );
    }

    public TareaRepositoryJdbc(JdbcTemplate jdbc, String jdbcUrl, TareaRowMapperJdbc tareaRowMapperJdbc, TareaMapperJdbc tareaMapperJdbc) {
        this.jdbc = jdbc;
        this.jdbcUrl = jdbcUrl;
        this.tareaRowMapperJdbc = tareaRowMapperJdbc;
        this.tareaMapperJdbc = tareaMapperJdbc;
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
    public List<Tarea> findAll() {
        // TODO: Recuperar las tareas de este almacenamiento.
        String query = "SELECT id, titulo FROM tareas_jdbc ORDER BY id";
        List<Tarea> tareas = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TareaEntityJdbc entity = tareaRowMapperJdbc.mapRow(rs, 0);
                if (entity != null) {
                    tareas.add(TareaMapperJdbc.toDomain(entity));
                }
            }
        } catch (SQLException e) {
            return List.of();
        }
        return tareas;
    }

    @Override
    public Optional<Tarea> findById(long id) {
        // TODO: Buscar una tarea por identificador.
        String query = "SELECT titulo FROM tareas_jdbc WHERE id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TareaEntityJdbc entity = tareaRowMapperJdbc.mapRow(rs, 0);
                    Tarea tarea = TareaMapperJdbc.toDomain(entity);
                    return Optional.of(tarea);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tarea create(Tarea tarea) {
        // TODO: Guardar la tarea nueva y devolverla con su identificador.
        String sql = "INSERT INTO tareas_jdbc (titulo) VALUES (?) ON CONFLICT(id) DO NOTHING";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, tarea.identificador());
            int result = ps.executeUpdate();
            return tarea;
        } catch (SQLException e) {
            return null; //
        }
    }

    @Override
    public Optional<Tarea> update(Tarea tarea) {
        // TODO: Actualizar una tarea existente.
        String sql = "UPDATE tareas_jdbc SET titulo = ? WHERE id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, tarea.identificador());
            ps.executeUpdate();
            return Optional.of(tarea);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long id) {
        // TODO: Eliminar la tarea y comunicar si existía.
        String sql = "DELETE FROM tareas_jdbc WHERE id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
