package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Vehiculo;
import es.ejemplo.taller.orm.config.OrmFactory;
import es.ejemplo.taller.orm.entity.ClienteEntity;
import es.ejemplo.taller.orm.entity.VehiculoEntity;
import es.ejemplo.taller.orm.mapper.ClienteMapper;
import es.ejemplo.taller.orm.mapper.VehiculoMapper;
import jakarta.persistence.EntityManagerFactory;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
public class VehiculoDao  {
    private final String url;
    private final EntityManagerFactory managerFactory;
    public VehiculoDao(String url) {
        this.url = url;
        managerFactory = OrmFactory.crear(url);
    }

    public List<Vehiculo> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery(
                            "select c from VehiculoEntity c order by c.nombre",
                            VehiculoEntity.class)
                    .getResultStream()
                    .map(VehiculoMapper::toDomain)
                    .toList();
        }
    }
    public Optional<Vehiculo> buscarPorId(String id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(VehiculoEntity.class, id))
                    .map(VehiculoMapper::toDomain);
        }
    }
    public int insertar(Vehiculo dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                VehiculoEntity entidad = VehiculoMapper.toEntity(dato);
                em.persist(entidad);
                em.flush(); // Necesario aquí para leer inmediatamente el ID generado
                dato.setIdCliente(entidad.getIdCliente());
                tx.commit();
                return dato.getIdCliente();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Vehiculo dato) {
        // TODO: implementar según el contrato del README.
        return 0;
    }
    public int eliminar(String id) {
        // TODO: implementar según el contrato del README.
        return 0;
    }
    public List<Vehiculo> buscarPorCliente(Integer idCliente) {
        // TODO: implementar según el contrato del README.
        return List.of();
    }
    public List<Vehiculo> listarPorMarca(String marca) {
        // TODO: implementar según el contrato del README.
        return List.of();
    }
}
