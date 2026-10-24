
package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.modelo.PiezaReparacion;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class PiezaReparacionDao extends AbstractDao<PiezaReparacion> {

    private final String url;

    public PiezaReparacionDao(String url) {
        this.url = url;
    }

    @Override
    public Optional<PiezaReparacion> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new PiezaReparacion(
                    rs.getInt("id_pieza"),
                    rs.getInt("id_reparacion"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("precio_unitario")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

    public List<PiezaReparacion> listar() {
        String query = "SELECT id_pieza, id_reparacion, nombre, cantidad, precio_unitario FROM piezas_reparacion";

        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public Optional<PiezaReparacion> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }

        String query = "SELECT id_pieza, id_reparacion, nombre, cantidad, precio_unitario FROM piezas_reparacion WHERE id_pieza = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public int insertar(PiezaReparacion dato) {
        if (dato == null
                || dato.getNombre() == null || dato.getNombre().isBlank()
                || dato.getCantidad() <= 0
                || dato.getPrecioUnitario() == null
                || dato.getPrecioUnitario().signum() < 0) {
            return 0;
        }

        String sql = "INSERT INTO piezas_reparacion (id_reparacion, nombre, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dato.getIdReparacion());
            ps.setString(2, dato.getNombre());
            ps.setInt(3, dato.getCantidad());
            ps.setBigDecimal(4, dato.getPrecioUnitario());
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

    public int actualizar(PiezaReparacion dato) {
        if (dato == null
                || dato.getNombre() == null || dato.getNombre().isBlank()
                || dato.getCantidad() <= 0
                || dato.getPrecioUnitario() == null
                || dato.getPrecioUnitario().signum() < 0) {
            return 0;
        }
        String sql = "UPDATE piezas_reparacion SET id_reparacion = ?, nombre = ?, cantidad = ?, precio_unitario = ? WHERE id_pieza = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dato.getIdReparacion());
            ps.setString(2, dato.getNombre());
            ps.setInt(3, dato.getCantidad());
            ps.setBigDecimal(4, dato.getPrecioUnitario());
            ps.setInt(5, dato.getIdPieza());
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }

        String sql = "DELETE FROM piezas_reparacion WHERE id_pieza = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public List<PiezaReparacion> listarPorReparacion(Integer id) {
        if (id == null) {
            return List.of();
        }

        String query = "SELECT id_pieza, id_reparacion, nombre, cantidad, precio_unitario FROM piezas_reparacion WHERE id_reparacion = ?";
        try (Connection con = ConexionBD.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    public BigDecimal calcularCostePiezas(Integer id) {
        if (id == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        List<PiezaReparacion> piezas = listarPorReparacion(id);
        for (PiezaReparacion pieza : piezas) {
            BigDecimal precio = pieza.getPrecioUnitario();
            int cantidad = pieza.getCantidad();
            total = total.add(precio.multiply(BigDecimal.valueOf(cantidad)));
        }
        return total;
    }
}
