package es.ejemplo.taller.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> {
    String url;

    protected abstract Optional<T> mapRsToClass(ResultSet rs);

    public List<T> listar(PreparedStatement ps) {
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
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                return mapRsToClass(rs);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
