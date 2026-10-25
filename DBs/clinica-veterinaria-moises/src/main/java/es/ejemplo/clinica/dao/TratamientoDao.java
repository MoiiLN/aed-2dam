package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.infra.ConexionDB;
import es.ejemplo.clinica.modelo.Cliente;
import es.ejemplo.clinica.modelo.Tratamiento;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public final class TratamientoDao extends AbstractDao<Tratamiento> implements ITratamientoDao {
    String url;
 public TratamientoDao(String url) { 
    this.url = url;
 }

    @Override
    public Optional<Tratamiento> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Tratamiento(
                    rs.getInt("id"),
                    rs.getInt("id_consulta"),
                    rs.getString("descripcion"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("precio_unitario")
            ));
        } catch (SQLException | RuntimeException e) {
            return Optional.empty();
        }
    }

 @Override
 public List<Tratamiento> listar() {
    // TODO Auto-generated method stub
     String query = "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos";
     try (Connection c = ConexionDB.abrir(url)) {
         PreparedStatement ps = c.prepareStatement(query);
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }

 @Override
 public Optional<Tratamiento> buscarPorId(Integer id) {
    // TODO Auto-generated method stub
     if (id == null || id < 0) {
         return Optional.empty();
     }
     String query = "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         ps.setInt(1, id);
         return buscarPorId(ps);
     } catch (SQLException e) {
         return Optional.empty();
     }
 }

 @Override
 public int insertar(Tratamiento entidad) {
    // TODO Auto-generated method stub
     if (entidad == null) {
         return 0;
     }

     String sql = "INSERT INTO tratamientos (id_consulta, descripcion, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setInt(1, entidad.getIdConsulta());
         ps.setString(2, entidad.getDescripcion());
         ps.setInt(3, entidad.getCantidad());
         ps.setBigDecimal(4, entidad.getPrecioUnitario());
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
 public int actualizar(Tratamiento entidad) {
    // TODO Auto-generated method stub
     if (entidad == null || entidad.getId() == null || entidad.getId() < 0) {
         throw new IllegalArgumentException();
     }
     String sql = "UPDATE tratamientos SET id_consulta = ?, descripcion = ?, cantidad = ?, precio_unitario = ? WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setInt(1, entidad.getIdConsulta());
         ps.setString(2, entidad.getDescripcion());
         ps.setInt(3, entidad.getCantidad());
         ps.setBigDecimal(4, entidad.getPrecioUnitario());
         ps.setInt(5, entidad.getId());
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
     String sql = "DELETE FROM tratamientos WHERE id = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setInt(1, id);
         return ejecutar(ps);
     } catch (SQLException e) {
         throw new DaoException(e.getMessage(), e.getCause());
     }
 }

 @Override
 public List<Tratamiento> listarPorConsulta(Integer idConsulta) {
    // TODO Auto-generated method stub
     if (idConsulta == null || idConsulta < 0) {
         return List.of();
     }
     String query = "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos WHERE id_consulta = ?";
     try (Connection con = ConexionDB.abrir(url);
          PreparedStatement ps = con.prepareStatement(query)) {
         ps.setInt(1, idConsulta);
         return listar(ps);
     } catch (SQLException e) {
         return List.of();
     }
 }

     @Override
     public BigDecimal calcularCosteTratamientos(Integer idConsulta) {
        // TODO Auto-generated method stub
         if (idConsulta == null || idConsulta < 0) {
             return BigDecimal.ZERO;
         }
         BigDecimal total = BigDecimal.ZERO;
         String query = "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos WHERE id_consulta = ?";
         try (Connection con = ConexionDB.abrir(url);
            PreparedStatement ps = con.prepareStatement(query)) {
             ps.setInt(1, idConsulta);
             List<Tratamiento> list = listar(ps);
             for (Tratamiento entidad : list) {
                 int cantidad = entidad.getCantidad();
                 BigDecimal precio = entidad.getPrecioUnitario();
                 total = total.add(BigDecimal.valueOf(cantidad).multiply(precio));
             }
             return total;
         } catch (Exception e) {
             return BigDecimal.ZERO;
         }
     }
}
