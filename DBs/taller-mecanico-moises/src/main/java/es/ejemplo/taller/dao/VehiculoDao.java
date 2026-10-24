
package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.modelo.Vehiculo;

import java.sql.*;
import java.util.*;

public class VehiculoDao extends AbstractDao<Vehiculo> {

    private final String url;

    public VehiculoDao(String url) {
        this.url = url;
    }

    @Override
    public Optional<Vehiculo> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Vehiculo(
                    rs.getString("matricula"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("anio"),
                    rs.getInt("id_cliente")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

    public List<Vehiculo> listar() {
        String query = "SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Optional<Vehiculo> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        String query = "SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos WHERE matricula = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public int insertar(Vehiculo dato) {
        if (dato == null
                || dato.getMatricula() == null || dato.getMatricula().isBlank()
                || dato.getMarca() == null || dato.getMarca().isBlank()
                || dato.getModelo() == null || dato.getModelo().isBlank()
                || dato.getAnio() <= 0) {
            return 0;
        }

        String sql = "INSERT INTO vehiculos (matricula, marca, modelo, anio, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getMatricula());
            ps.setString(2, dato.getMarca());
            ps.setString(3, dato.getModelo());
            ps.setInt(4, dato.getAnio());
            ps.setInt(5, dato.getIdCliente());
            return ps.executeUpdate() > 0 ? 1 : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizar(Vehiculo dato) {
        if (dato == null
                || dato.getMatricula() == null || dato.getMatricula().isBlank()
                || dato.getMarca() == null || dato.getMarca().isBlank()
                || dato.getModelo() == null || dato.getModelo().isBlank()
                || dato.getAnio() <= 0) {
            return 0;
        }

        String sql = "UPDATE vehiculos SET marca = ?, modelo = ?, anio = ?, id_cliente = ? WHERE matricula = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getMarca());
            ps.setString(2, dato.getModelo());
            ps.setInt(3, dato.getAnio());
            ps.setInt(4, dato.getIdCliente());
            ps.setString(5, dato.getMatricula());
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int eliminar(String id) {
        if (id == null || id.isBlank()) {
            return 0;
        }

        String sql = "DELETE FROM vehiculos WHERE matricula = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public List<Vehiculo> buscarPorCliente(Integer idCliente) {
        if (idCliente == null) {
            return List.of();
        }

        String query = "SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos WHERE id_cliente = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Vehiculo> listarPorMarca(String marca) {
        if (marca == null || marca.isBlank()) {
            return List.of();
        }

        String query = "SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos WHERE marca = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, marca);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }
}
