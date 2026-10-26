package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.infra.ConexionDB;
import es.ejemplo.clinica.modelo.Cliente;
import es.ejemplo.clinica.modelo.Consulta;
import java.sql.*;
import java.util.*;

public final class ConsultaDao extends AbstractDao<Consulta> implements IConsultaDao {
    String url;
 public ConsultaDao(String url) { 
    this.url = url;
  }


    @Override
    public Optional<Consulta> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Consulta(
                    rs.getInt("id"),
                    rs.getInt("id_mascota"),
                    rs.getInt("id_veterinario"),
                    rs.getString("fecha"),
                    rs.getBigDecimal("horas"),
                    rs.getString("estado")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }
 @Override
 public List<Consulta> listar() {
    // TODO Auto-generated method stub
     String query = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas";
     try (Connection c = ConexionDB.abrir(url)) {
         PreparedStatement ps = c.prepareStatement(query);
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }

 @Override
 public Optional<Consulta> buscarPorId(Integer id) {
    // TODO Auto-generated method stub
     if (id == null || id < 0) {
         return Optional.empty();
     }
     String query = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         ps.setInt(1, id);
         return buscarPorId(ps);
     } catch (SQLException e) {
         return Optional.empty();
     }
 }

 @Override
 public int insertar(Consulta entidad) {
    // TODO Auto-generated method stub
     if (entidad == null) {
         return 0;
     }

     String sql = "INSERT INTO consultas (id_mascota, id_veterinario, fecha, horas, estado) VALUES (?, ?, ?, ?, ?)";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setInt(1, entidad.getIdMascota());
         ps.setInt(2, entidad.getIdVeterinario());
         ps.setString(3, entidad.getFecha());
         ps.setBigDecimal(4, entidad.getHoras());
         ps.setString(5, entidad.getEstado());
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
 public int actualizar(Consulta entidad) {
    // TODO Auto-generated method stub
     if (entidad == null) {
         throw new IllegalArgumentException();
     }
     String sql = "UPDATE consultas SET id_mascota = ?, id_veterinario = ?, fecha = ?, horas = ?, estado = ? WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setInt(1, entidad.getIdMascota());
         ps.setInt(2, entidad.getIdVeterinario());
         ps.setString(3, entidad.getFecha());
         ps.setBigDecimal(4, entidad.getHoras());
         ps.setString(5, entidad.getEstado());
         ps.setInt(6, entidad.getId());
         return ejecutar(ps);
     } catch (SQLException e) {
         return 0;
     }
 }

 @Override
 public int eliminar(Integer id) {
    // TODO Auto-generated method stub
     if (id == null || id < 0) {
         throw new NullPointerException();
     }
     String sql = "DELETE FROM consultas WHERE id = ?";
     comprobarConsultaConTratamiento(id);
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setInt(1, id);
         return ejecutar(ps);
     } catch (SQLException e) {
         throw new DaoException(e.getMessage(), e.getCause());
     }
 }

 @Override
 public List<Consulta> buscarPorMascota(Integer idMascota) {
    // TODO Auto-generated method stub
     if (idMascota == null || idMascota < 0) {
         return List.of();
     }
     String query = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         ps.setInt(1, idMascota);
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }

 @Override
 public List<Consulta> buscarPorVeterinario(Integer idVeterinario) {
    // TODO Auto-generated method stub
     if (idVeterinario == null || idVeterinario < 0) {
         return List.of();
     }
     String query = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id_veterinario = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         ps.setInt(1, idVeterinario);
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }

 @Override
 public List<Consulta> listarProgramadas() {
    // TODO Auto-generated method stub
     String query = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE estado = 'PROGRAMADA'";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }
    public void comprobarConsultaConTratamiento(Integer id) {
        String query = "SELECT COUNT(*) FROM tratamientos WHERE id_consulta = ?";
        try (Connection con = ConexionDB.abrir(url);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int cantidad = rs.getInt(1);
                    if (cantidad > 0) {
                        throw new DaoException("La consulta tiene tratamiento", null);
                    }
                }
            }
        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }
}
