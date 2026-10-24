
package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.modelo.Reparacion;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class ReparacionDao extends AbstractDao<Reparacion> {

    private final String url;

    public ReparacionDao(String url) {
        this.url = url;
    }

    @Override
    public Optional<Reparacion> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Reparacion(
                    rs.getInt("id_reparacion"),
                    rs.getString("matricula"),
                    rs.getInt("id_mecanico"),
                    rs.getString("descripcion"),
                    rs.getString("fecha"),
                    rs.getBigDecimal("horas_trabajo"),
                    rs.getString("estado")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

    public List<Reparacion> listar() {
        String query = "SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Optional<Reparacion> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }

        String query = "SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE id_reparacion = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public int insertar(Reparacion dato) {
        if (dato == null
                || dato.getMatricula() == null || dato.getMatricula().isBlank()
                || dato.getDescripcion() == null || dato.getDescripcion().isBlank()
                || dato.getFecha() == null || dato.getFecha().isBlank()
                || dato.getHorasTrabajo() == null || dato.getHorasTrabajo().signum() < 0
                || dato.getEstado() == null || dato.getEstado().isBlank()) {
            return 0;
        }

        String sql = "INSERT INTO reparaciones (id_mecanico, matricula, descripcion, fecha, horas_trabajo, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dato.getIdMecanico());
            ps.setString(2, dato.getMatricula());
            ps.setString(3, dato.getDescripcion());
            ps.setString(4, dato.getFecha());
            ps.setBigDecimal(5, dato.getHorasTrabajo());
            ps.setString(6, dato.getEstado());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
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

    public int actualizar(Reparacion dato) {
        if (dato == null) {
            return 0;
        }

        String sql = "UPDATE reparaciones SET matricula = ?, id_mecanico = ?, descripcion = ?, fecha = ?, horas_trabajo = ?, estado = ? WHERE id_reparacion = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getMatricula());
            ps.setInt(2, dato.getIdMecanico());
            ps.setString(3, dato.getDescripcion());
            ps.setString(4, dato.getFecha());
            ps.setBigDecimal(5, dato.getHorasTrabajo());
            ps.setString(6, dato.getEstado());
            ps.setInt(7, dato.getIdReparacion());
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }

        String sql = "DELETE FROM reparaciones WHERE id_reparacion = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public List<Reparacion> buscarPorVehiculo(String valor) {
        if (valor == null || valor.isBlank()) {
            return List.of();
        }

        String query = "SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE matricula = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, valor);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Reparacion> buscarPorMecanico(Integer valor) {
        if (valor == null) {
            return List.of();
        }

        String query = "SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE id_mecanico = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, valor);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public List<Reparacion> listarPendientes() {
        String query = "SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE estado = 'PENDIENTE'";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }
}
