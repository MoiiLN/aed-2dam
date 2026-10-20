package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.model.Vehiculo;
import java.sql.*;
import java.util.*;

public class VehiculoDao extends AbstractDao<Vehiculo> {
    public VehiculoDao(String url) { super(url); }

    @Override
    protected Optional<Vehiculo> mapRsToClass(ResultSet rs) {
        // TODO: leer matricula, marca, modelo, anio con rs.getXXX(...)
        // Capturar SQLException y RuntimeException aquí; nunca declarar throws.
        return Optional.empty();
    }

    public List<Vehiculo> listar() {
        String sql = "SELECT matricula, marca, modelo, anio FROM Vehiculos ORDER BY matricula";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return consultar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("listar productos", e);
            return List.of();
        }
        // TODO: seguir el ejemplo de ProductoDao.listar()
    }

    public Optional<Vehiculo> buscarPorId(String matricula) {
        String query = "SELECT id, nombre, precio FROM Productos WHERE matricula = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, matricula);
            return consultarUno(ps);
        } catch (Exception e) {
            return Optional.empty();
        }
        // TODO: SELECT WHERE matricula = ?; ps.setString(1, matricula)
    }

    public Optional<Vehiculo> buscarPorMatricula(String matricula) {
        String query = "SELECT matricula, marca, modelo, anio FROM Productos WHERE matricula = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, matricula);
            return consultarUno(ps);
        } catch (Exception e) {
            return Optional.empty();
        }
        // TODO: delegar en buscarPorId(matricula)
    }

    public boolean eliminar(String matricula) {
        String sql = "DELETE FROM Productos WHERE matricula = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, matricula);
            return ejecutar(ps);
        } catch (Exception e){
            return false;
        }
        // TODO: DELETE WHERE matricula = ?
    }

    public boolean insertar(Vehiculo v) {
        String sql = "INSERT INTO Vehiculos VALUES (?, ?, ?, ?)";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnio());
            return ejecutar(ps);
        } catch (Exception e) {
            return false;
        }
        // TODO: validar e insertar
    }

    public boolean actualizar(Vehiculo v) {
        String sql = "UPDATE Productos SET marca = ?, modelo = ?, anio = ? WHERE matricula = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, v.getMarca());
            ps.setString(2, v.getModelo());
            ps.setInt(3, v.getAnio());
            ps.setString(4, v.getMatricula());
            return ejecutar(ps);
        } catch (Exception e) {
            return false;
        }
        // TODO: validar y actualizar
    }

    public List<Vehiculo> buscarPorMarca(String texto) {
        String query = "SELECT matricula, marca, modelo, anio FROM Productos WHERE marca = ?";
        try (Connection connection = this.obtenerConexion()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, texto);
            return consultar(ps);
        } catch (Exception e) {
            return List.of();
        }
        // TODO: LIKE sin distinguir mayúsculas
    }

    public List<Vehiculo> buscarPorAnio(int desde, int hasta) {
        // TODO: BETWEEN inclusivo
        return List.of();
    }

    public int contar() {
        // TODO: consultarEntero(ps)
        return 0;
    }

    public Map<String, Integer> contarPorMarca() {
        // TODO: GROUP BY marca y consultarAgrupado(ps)
        return Map.of();
    }
}
