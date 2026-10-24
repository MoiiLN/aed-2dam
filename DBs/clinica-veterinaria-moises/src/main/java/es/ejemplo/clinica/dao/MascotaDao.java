package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.infra.ConexionDB;
import es.ejemplo.clinica.modelo.Cliente;
import es.ejemplo.clinica.modelo.Mascota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class MascotaDao extends AbstractDao<Mascota> implements IMascotaDao {
    String url;

    public MascotaDao(String url) {
        this.url = url;
    }

    @Override
    public Optional<Mascota> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Mascota(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    rs.getString("raza"),
                    rs.getString("fecha_nacimiento"),
                    rs.getInt("id_cliente")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Mascota> listar() {
        // TODO Auto-generated method stub
        String query = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas";
        try (Connection c = ConexionDB.abrir(url)) {
            PreparedStatement ps = c.prepareStatement(query);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public Optional<Mascota> buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        if (id == null || id < 0) {
            return Optional.empty();
        }
        String query = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE id = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            return buscarPorId(ps);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public int insertar(Mascota entidad) {
        // TODO Auto-generated method stub
        if (entidad == null || entidad.getNombre() == null || entidad.getNombre().isBlank() || entidad.getIdCliente() < 0) {
            return 0;
        }
        String sql = "INSERT INTO mascotas (nombre, especie, raza, fecha_nacimiento, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecie());
            ps.setString(3, entidad.getRaza());
            ps.setString(4, entidad.getFechaNacimiento());
            ps.setInt(5, entidad.getIdCliente());
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
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return 0;
    }

    @Override
    public int actualizar(Mascota entidad) {
        // TODO Auto-generated method stub
        if (entidad == null || entidad.getId() == null || entidad.getId() < 0) {
            throw new IllegalArgumentException();
        }
        String sql = "UPDATE mascotas SET nombre = ?, especie = ?, raza = ?, fecha_nacimiento = ?, id_cliente = ? WHERE id = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecie());
            ps.setString(3, entidad.getRaza());
            ps.setString(4, entidad.getFechaNacimiento());
            ps.setInt(5, entidad.getIdCliente());
            ps.setInt(6, entidad.getId());
            return ejecutar(ps);
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public int eliminar(Integer id) {
        // TODO Auto-generated method stub
        if (id == null) {
            throw new NullPointerException();
        }
        String sql = "DELETE FROM mascotas WHERE id = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ejecutar(ps);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Mascota> buscarPorCliente(Integer idCliente) {
        // TODO Auto-generated method stub
        if (idCliente == null || idCliente < 0) {
            return List.of();
        }
        String query = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE id = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public List<Mascota> listarPorEspecie(String especie) {
        // TODO Auto-generated method stub
        if (especie == null) {
            return List.of();
        }
        String query = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE especie = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, especie);
            return listar(ps);
        } catch (SQLException e) {
            return List.of();
        }
    }
}


