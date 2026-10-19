package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.config.ConexionBD;
import es.ejemplo.gestion.exception.AplicationException;
import es.ejemplo.gestion.model.Producto;
import es.ejemplo.gestion.model.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VehiculoDao extends AbstractDao {
    private  ConexionBD conexionBD;

    public VehiculoDao(String url) {
        super(url);
    }

    @Override
    Vehiculo mapToClass(ResultSet rs) {
        try {
            return new Vehiculo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
    }

    public List<Vehiculo> listar() {
        List<Vehiculo> vehicles = new ArrayList<>();
        String query = "SELECT matricula, marca, modelo, anio FROM Vehiculos ORDER BY matricula ASC";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vehicles.add(mapToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return vehicles;
    }

    public Optional<Vehiculo> buscarPorMatricula(String matricula) {
        String query = "SELECT matricula, marca, modelo, anio FROM Vehiculos WHERE matricula = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return Optional.empty();
    }

    public boolean insertar(Vehiculo vehiculo) {
        String sql = "INSERT INTO Vehiculos VALUES (?, ?, ?, ?)";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, vehiculo.getMatricula());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return false;
    }

    public boolean actualizar(Vehiculo vehiculo) {
        String sql = "UPDATE Vehiculos SET marca = ?, modelo = ?, anio = ? WHERE matricula = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, vehiculo.getMarca());
            ps.setString(2, vehiculo.getModelo());
            ps.setInt(3, vehiculo.getAnio());
            ps.setString(4, vehiculo.getMatricula());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return false;
    }

    public boolean eliminar(String matricula) {
        String sql = "DELETE FROM Vehiculos WHERE matricula = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, matricula);
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return false;
    }

    public List<Vehiculo> buscarPorMarca(String texto) {
        List<Vehiculo> vehicles = new ArrayList<>();
        String query = "SELECT matricula, marca, modelo, anio FROM Vehiculos WHERE marca = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, texto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vehicles.add(mapToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return vehicles;
    }

    public List<Vehiculo> buscarPorAnio(int desde, int hasta) {
        List<Vehiculo> vehicles = new ArrayList<>();
        String query = "SELECT matricula, marca, modelo, anio FROM Vehiculos WHERE anio BETWEEN ? AND ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, desde);
            ps.setInt(1, hasta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vehicles.add(mapToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return vehicles;
    }

    public int contar() {
        String query = "SELECT COUNT(*) FROM Vehiculos";
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

    public Map<String, Integer> contarPorMarca() {
        Map<String, Integer> finalMap = new HashMap<>();
        String query = "SELECT marca, COUNT(*) FROM Vehiculos GROUP BY marca";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String marca = rs.getString(1);
                int cantidad = rs.getInt(2);

                finalMap.put(marca, cantidad);
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }

        return finalMap;
    }
}
