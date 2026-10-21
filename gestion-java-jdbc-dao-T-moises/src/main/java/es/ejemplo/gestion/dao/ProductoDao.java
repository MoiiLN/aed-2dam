package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.model.Producto;
import java.sql.*;
import java.util.*;

public class ProductoDao extends AbstractDao<Producto> {
    public ProductoDao(String url) { super(url); }

    /** EJEMPLO COMPLETO: transformar ResultSet a Producto sin propagar excepciones. */
    @Override
    protected Optional<Producto> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio")));
        } catch (SQLException | RuntimeException e) {
            registrar("mapRsToClass producto", e);
            return Optional.empty();
        }
    }

    /** EJEMPLO COMPLETO: listar productos mediante PreparedStatement. */
    public List<Producto> listar() {
        String sql = "SELECT id,nombre,precio FROM Productos ORDER BY id";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return consultar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("listar productos", e);
            return List.of();
        }
    }

    // TODO: implementa cada método con PreparedStatement y try-with-resources.
    // IMPORTANTE: buscarPorId y eliminar usan Integer; la clase base NO necesita ID.
    public Optional<Producto> buscarPorId(Integer id) {
        String query = "SELECT id, nombre, precio FROM Productos WHERE id = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            return consultarUno(ps);
        } catch (Exception e) {
            return Optional.empty();
        }
        // TODO: SELECT ... WHERE id = ?; ps.setInt(1, id); consultarUno(ps)
    }

    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM Productos WHERE id = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ejecutar(ps);
        } catch (Exception e){
            return false;
        }
        // TODO: DELETE ... WHERE id = ?; ejecutar(ps)
    }

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO Productos VALUES (?, ?, ?)";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setDouble(3, p.getPrecio());
            return ejecutar(ps);
        } catch (Exception e) {
            return false;
        }
        // TODO: validar, INSERT con tres parámetros, ejecutar(ps)
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE Productos SET nombre = ?, precio = ? WHERE id = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getId());
            return ejecutar(ps);
        } catch (Exception e) {
            return false;
        }
        // TODO: validar, UPDATE con parámetros, ejecutar(ps)
    }

    public List<Producto> buscarPorNombre(String texto) {
        String query = "SELECT id, nombre, precio FROM Productos WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY id DESC";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + texto + "%");
            return consultar(ps);
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Producto> buscarPorPrecio(double minimo, double maximo) {
        String query = "SELECT id, nombre, precio FROM Productos WHERE precio BETWEEN ? AND ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDouble(1, minimo);
            ps.setDouble(2, maximo);
            return consultar(ps);
        } catch (Exception e) {
            return List.of();
        }
        // TODO: BETWEEN inclusivo, ordenado por id
    }

    public int contar() {
        String query = "SELECT COUNT(*) FROM Productos";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            return consultarEntero(ps);
        } catch (Exception e) {
            return 0;
        }
        // TODO: SELECT COUNT(*) y consultarEntero(ps)
    }

    public double precioMedio() {
        String query = "SELECT AVG(precio) FROM Productos";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            return consultarDecimal(ps);
        } catch (Exception e) {
            return 0.0;
        }
        // TODO: COALESCE(AVG(precio),0) y consultarDecimal(ps)
    }
}
