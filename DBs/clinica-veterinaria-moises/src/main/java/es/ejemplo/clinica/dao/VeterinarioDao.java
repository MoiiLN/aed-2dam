package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.infra.ConexionDB;
import es.ejemplo.clinica.modelo.Cliente;
import es.ejemplo.clinica.modelo.Veterinario;
import java.sql.*;
import java.util.*;

public final class VeterinarioDao extends AbstractDao<Veterinario> implements IVeterinarioDao {
    String url;
 public VeterinarioDao(String url) { 
    this.url = url;
 }

    @Override
    protected Optional<Veterinario> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Veterinario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getBigDecimal("tarifa_hora")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }
 @Override public List<Veterinario> listar() {
    // TODO Auto-generated method stub
     String query = "SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }
 @Override public Optional<Veterinario> buscarPorId(Integer id) {
    // TODO Auto-generated method stub
     if (id == null || id < 0) {
         return Optional.empty();
     }
     String query = "SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         ps.setInt(1, id);
         return buscarPorId(ps);
     } catch (SQLException e) {
         return Optional.empty();
     }
 }
 @Override public int insertar(Veterinario entidad) {
    // TODO Auto-generated method stub
     if (entidad == null || entidad.getNombre() == null || entidad.getNombre().isBlank()) {
         return 0;
     }

     String sql = "INSERT INTO veterinarios (nombre, especialidad, tarifa_hora) VALUES (?, ?, ?)";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setString(1, entidad.getNombre());
         ps.setString(2, entidad.getEspecialidad());
         ps.setBigDecimal(3, entidad.getTarifaHora());
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
 @Override public int actualizar(Veterinario entidad) {
    // TODO Auto-generated method stub
     if (entidad == null || entidad.getId() == null || entidad.getId() < 0) {
         throw new IllegalArgumentException();
     }
     String sql = "UPDATE veterinarios SET nombre = ?, especialidad = ?, tarifa_hora = ? WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setString(1, entidad.getNombre());
         ps.setString(2, entidad.getEspecialidad());
         ps.setBigDecimal(3, entidad.getTarifaHora());
         ps.setInt(4, entidad.getId());
         return ejecutar(ps);
     } catch (SQLException e) {
         return 0;
     }
 }
 @Override public int eliminar(Integer id) {
    // TODO Auto-generated method stub
     if (id == null) {
         throw new NullPointerException();
     }
     String sql = "DELETE FROM veterinarios WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setInt(1, id);
         return ejecutar(ps);
     } catch (SQLException e) {
         throw new DaoException(e.getMessage(), e.getCause());
     }
 }
 @Override
 public List<Veterinario> buscarPorEspecialidad(String especialidad) {
    // TODO Auto-generated method stub
     if (especialidad == null) {
         return List.of();
     }
     String query = "SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios WHERE especialidad = ? ORDER BY tarifa_hora ASC";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         ps.setString(1, especialidad);
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }

}

 