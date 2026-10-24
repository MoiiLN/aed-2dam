
package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.modelo.Mecanico;

import java.sql.*;
import java.util.*;

public class MecanicoDao extends AbstractDao<Mecanico> {

    private final String url;

    public MecanicoDao(String url) {
        this.url = url;
    }

    @Override
    protected Optional<Mecanico> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Mecanico(
                    rs.getInt("id_mecanico"),
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getBigDecimal("tarifa_hora")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

    public List<Mecanico> listar() {
        String query = "SELECT id_mecanico, nombre, especialidad, tarifa_hora FROM mecanicos";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Optional<Mecanico> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        String query = "SELECT id_mecanico, nombre, especialidad, tarifa_hora FROM mecanicos WHERE id_mecanico = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public int insertar(Mecanico dato) {
        if (dato == null
                || dato.getNombre() == null || dato.getNombre().isBlank()
                || dato.getEspecialidad() == null || dato.getEspecialidad().isBlank()
                || dato.getTarifaHora() == null
                || dato.getTarifaHora().signum() < 0) {
            return 0;
        }
        String sql = "INSERT INTO mecanicos (nombre, especialidad, tarifa_hora) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getNombre());
            ps.setString(2, dato.getEspecialidad());
            ps.setBigDecimal(3, dato.getTarifaHora());
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

    public int actualizar(Mecanico dato) {
        if (dato == null
                || dato.getNombre() == null || dato.getNombre().isBlank()
                || dato.getEspecialidad() == null || dato.getEspecialidad().isBlank()
                || dato.getTarifaHora() == null
                || dato.getTarifaHora().signum() < 0) {
            return 0;
        }
        String sql = "UPDATE mecanicos SET nombre = ?, especialidad = ?, tarifa_hora = ? WHERE id_mecanico = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getNombre());
            ps.setString(2, dato.getEspecialidad());
            ps.setBigDecimal(3, dato.getTarifaHora());
            ps.setInt(4, dato.getIdMecanico());
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }
        String sql = "DELETE FROM mecanicos WHERE id_mecanico = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public List<Mecanico> buscarPorEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.isBlank()) {
            return List.of();
        }
        String query = "SELECT id_mecanico, nombre, especialidad, tarifa_hora FROM mecanicos WHERE especialidad = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, especialidad);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }
}
