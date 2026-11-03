package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.infra.ConexionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> {

    protected abstract Optional<T> mapRsToClass(ResultSet rs);

    public List<T> listar(PreparedStatement ps) {
        if (ps == null) {
            return List.of();
        }
        try (ResultSet rs = ps.executeQuery()) {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                Optional<T> entidad = mapRsToClass(rs);
                if (entidad.isEmpty()) {
                    return List.of();
                }
                list.add(entidad.get());
            }
            return list;
        } catch (Exception e) {
            return List.of();
        }
    }
    public Optional<T> buscarPorId(PreparedStatement ps) {
        if (ps == null) {
            return Optional.empty();
        }
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapRsToClass(rs);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
    protected int ejecutar(PreparedStatement ps) {
        if (ps == null) {
            return 0;
        }
        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }
}

// protected abstract Optional<T> mapRsToClass(ResultSet rs);

// protected List<T> consultar(PreparedStatement ps) {
//     if (ps == null) return List.of();
//
//     try (ResultSet rs = ps.executeQuery()) {
//         List<T> resultado = new ArrayList<>();
//
//         while (rs.next()) {
//             Optional<T> entidad = mapRsToClass(rs);
//
//             if (entidad.isEmpty()) return List.of();
//
//             resultado.add(entidad.get());
//         }
//
//         return resultado;
//
//     } catch (SQLException | RuntimeException e) {
//
//         return List.of();
//     }
// }
//
// protected Optional<T> consultarUno(PreparedStatement ps) {
//     if (ps == null) return Optional.empty();
//
//     try (ResultSet rs = ps.executeQuery()) {
//         return rs.next() ? mapRsToClass(rs) : Optional.empty();
//     } catch (SQLException | RuntimeException e) {
//         return Optional.empty();
//     }
// }
//
// protected boolean ejecutar(PreparedStatement ps) {
//     if (ps == null) return false;
//
//     try {
//         return ps.executeUpdate() > 0;
//
//     } catch (SQLException | RuntimeException e) {
//
//         return false;
//     }
// }
//
// protected int consultarEntero(PreparedStatement ps) {
//     if (ps == null) return 0;
//
//     try (ResultSet rs = ps.executeQuery()) {
//         return rs.next() ? rs.getInt(1) : 0;
//
//     } catch (SQLException | RuntimeException e) {
//
//         return 0;
//     }
// }
//
// protected double consultarDecimal(PreparedStatement ps) {
//     if (ps == null) return 0;
//
//     try (ResultSet rs = ps.executeQuery()) {
//         return rs.next() ? rs.getDouble(1) : 0;
//
//     } catch (SQLException | RuntimeException e) {
//
//         return 0;
//     }
// }
//
// protected Map<String, Integer> consultarAgrupado(PreparedStatement ps) {
//     if (ps == null) return Map.of();
//
//     try (ResultSet rs = ps.executeQuery()) {
//         Map<String, Integer> mapa = new LinkedHashMap<>();
//
//         while (rs.next()) {
//             mapa.put(rs.getString(1), rs.getInt(2));
//         }
//
//         return mapa;
//
//     } catch (SQLException | RuntimeException e) {
//
//         return Map.of();
//     }
// }
//