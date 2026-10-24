package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.config.ConexionBD;
import es.ejemplo.gestion.exception.AplicationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> extends ConexionBD {

    public AbstractDao(String url) {
        super(url);
    }

    abstract T mapToClass(ResultSet rs);

    public List<T> consultar(PreparedStatement ps) {
        List<T> products = new ArrayList<>();
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(mapToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return products;
    }
}
