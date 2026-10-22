package es.ejemplo.taller.dao;
import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.modelo.Cliente;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
public class ClienteDao extends AbstractDao<Cliente> {
    private final String url;
    public ClienteDao(String url) {
        super(url);
        this.url = url; }

    public Optional<Cliente> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Cliente(rs.getInt("id_cliente"), rs.getString("nombre"), rs.getString("email"), rs.getString("telefono")));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }
    public List<Cliente> listar() {
        // TODO: implementar según el contrato del README.
        String query = "SELECT id_cliente, nombre, email, telefono FROM clientes";
        try (Connection con = ConexionBD.abrir(url)) {
            PreparedStatement ps = con.prepareStatement(query);
            return listar(ps);
        } catch (Exception e) {
            return List.of();
        }
    }
    public Optional<Cliente> buscarPorId(Integer id) {
        // TODO: implementar según el contrato del README.
        String query = "SELECT id_cliente, nombre, email, telefono FROM clientes WHERE id = ?";
        try (Connection con = obtenerConexion()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    public int insertar(Cliente dato) {
        // TODO: implementar según el contrato del README.
        String sql = "INSERT INTO Clientes VALUES(?, ?, ?)";
        try (Connection con = obtenerConexion()) {
            PreparedStatement ps = con.prepareStatement(sql);
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
    public int actualizar(Cliente dato) {
        // TODO: implementar según el contrato del README.
        return 0;
    }
    public int eliminar(Integer id) {
        // TODO: implementar según el contrato del README.
        return 0;
    }
}
