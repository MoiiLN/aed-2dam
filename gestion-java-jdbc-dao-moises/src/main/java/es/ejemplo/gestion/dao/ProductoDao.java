package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.config.ConexionBD;
import es.ejemplo.gestion.exception.AplicationException;
import es.ejemplo.gestion.model.Producto;

import javax.management.InvalidApplicationException;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ProductoDao extends AbstractDao {
    private  ConexionBD conexionBD;

    public ProductoDao(String url) {
        super(url);
    }

    @Override
    Producto mapToClass(ResultSet rs) {
        try {
            return new Producto(rs.getInt(1), rs.getString(2), rs.getDouble(3));
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
    }

    public List<Producto> listar() {
        String query = "SELECT id, nombre, precio FROM Productos";
        return this.consultar(query);
    }

    public Optional<Producto> buscarPorId(int id) {
        String query = "SELECT id, nombre, precio FROM Productos WHERE id = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return Optional.empty();
    }

    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO Productos VALUES (?, ?, ?)";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, producto.getId());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecio());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return false;
    }

    public boolean actualizar(Producto producto) {
        String sql = "UPDATE Productos SET nombre = ?, precio = ? WHERE id = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getId());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Productos WHERE id = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return false;
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> products = new ArrayList<>();
       String query = "SELECT id, nombre, precio FROM Productos WHERE nombre = ?";
       try (Connection connection = this.obtenerConexion()) {
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, texto);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            products.add(mapToClass(rs));
        }
       } catch (Exception e) {
           throw new AplicationException(e.getMessage(), e.getCause());
       }
       return products;
    }

    public List<Producto> buscarPorPrecio(double minimo, double maximo) {
        List<Producto> products = new ArrayList<>();
        String query = "SELECT id, nombre, precio FROM Productos WHERE precio BETWEEN ? AND ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDouble(1, minimo);
            ps.setDouble(2, maximo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(mapToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return products;
    }

    public int contar() {
        String query = "SELECT COUNT(*) FROM Productos";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return 0;
    }

    public double precioMedio() {
        String query = "SELECT AVG(precio) FROM Productos";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return 0.0;
    }
}
