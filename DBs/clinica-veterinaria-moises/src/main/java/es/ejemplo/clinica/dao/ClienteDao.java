package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.infra.ConexionDB;
import es.ejemplo.clinica.modelo.Cliente;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public final class ClienteDao extends AbstractDao<Cliente> implements IClienteDao {
    String url;

    public ClienteDao(String url) {
        this.url = url;
    }

    @Override
    public Optional<Cliente> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Cliente> listar() {
        String query = "SELECT id, nombre, email, telefono FROM clientes";
        try (Connection c = ConexionDB.abrir(url)) {
            PreparedStatement ps = c.prepareStatement(query);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        if (id == null || id < 0) {
            return Optional.empty();
        }
        String query = "SELECT id, nombre, email, telefono FROM clientes WHERE id = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public int insertar(Cliente entidad) {
        // TODO Auto-generated method stub
        if (entidad == null || entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            return 0;
        }

        String sql = "INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEmail());
            ps.setString(3, entidad.getTelefono());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) { // Obtiene la ID asignado al registro recientemente insertado en la DB.
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        if (idGenerado > 0) {
                            return idGenerado;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return 0;
    }

    @Override
    public int actualizar(Cliente entidad) {
        // TODO Auto-generated method stub
        if (entidad == null || entidad.getId() == null || entidad.getId() < 0) {
            throw new IllegalArgumentException();
        }
        String sql = "UPDATE clientes SET nombre = ?, email = ?, telefono = ? WHERE id = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEmail());
            ps.setString(3, entidad.getTelefono());
            ps.setInt(4, entidad.getId());
            return ejecutar(ps);
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public int eliminar(Integer id) {
        // TODO Auto-generated method stub
        if (id == null) {
            throw new NullPointerException();
        }
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ejecutar(ps);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }
}
