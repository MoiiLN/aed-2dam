
package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.modelo.Cliente;

import java.sql.*;
import java.util.*;

public class ClienteDao extends AbstractDao<Cliente> {

    private final String url;

    public ClienteDao(String url) {
        this.url = url;
    }

    @Override
    public Optional<Cliente> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

    public List<Cliente> listar() {
        String query = "SELECT id_cliente, nombre, email, telefono FROM clientes";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }

        String query = "SELECT id_cliente, nombre, email, telefono FROM clientes WHERE id_cliente = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public int insertar(Cliente dato) {
        if (dato == null || dato.getNombre() == null || dato.getNombre().isBlank()) {
            return 0;
        }

        String sql = "INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getNombre());
            ps.setString(2, dato.getEmail());
            ps.setString(3, dato.getTelefono());
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
            return 0;
        }
        return 0;
    }

    public int actualizar(Cliente dato) {
        if (dato == null || dato.getNombre() == null || dato.getNombre().isBlank()) {
            return 0;
        }
        String sql = "UPDATE clientes SET nombre = ?, email = ?, telefono = ? WHERE id_cliente = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getNombre());
            ps.setString(2, dato.getEmail());
            ps.setString(3, dato.getTelefono());
            ps.setInt(4, dato.getIdCliente());
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }
}
