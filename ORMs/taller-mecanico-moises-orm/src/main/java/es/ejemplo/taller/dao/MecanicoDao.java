package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Mecanico;
import es.ejemplo.taller.orm.config.OrmFactory;
import es.ejemplo.taller.orm.entity.ClienteEntity;
import es.ejemplo.taller.orm.entity.MecanicoEntity;
import es.ejemplo.taller.orm.entity.VehiculoEntity;
import es.ejemplo.taller.orm.mapper.ClienteMapper;
import es.ejemplo.taller.orm.mapper.MecanicoMapper;
import es.ejemplo.taller.orm.mapper.VehiculoMapper;
import jakarta.persistence.EntityManagerFactory;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
public class MecanicoDao  {
    private final String url;
    private final EntityManagerFactory managerFactory;
    public MecanicoDao(String url) {
        this.url = url;
        managerFactory = OrmFactory.crear(url);

    }
    
    public List<Mecanico> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery(
                            "select m from MecanicoEntity m",
                            MecanicoEntity.class)
                    .getResultStream()
                    .map(MecanicoMapper::toDomain)
                    .toList();
        }
    }
    public Optional<Mecanico> buscarPorId(Integer id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(MecanicoEntity.class, id))
                    .map(MecanicoMapper::toDomain);
        }
    }
    public int insertar(Mecanico dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                MecanicoEntity entidad = MecanicoMapper.toEntity(dato);
                em.persist(entidad);
                em.flush(); // Necesario aquí para leer inmediatamente el ID generado
                dato.setIdMecanico(entidad.getIdMecanico());
                tx.commit();
                return dato.getIdMecanico();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Mecanico dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                MecanicoEntity m = em.find(MecanicoEntity.class, dato.getIdMecanico());
                if (m == null) {
                    tx.commit();
                    return 0;
                }
                MecanicoEntity mecanicoEntityActualizado = MecanicoMapper.toEntity(dato);
                m.setNombre(mecanicoEntityActualizado.getNombre());// dirty checking
                m.setEspecialidad(mecanicoEntityActualizado.getEspecialidad());
                m.setTarifaHora(mecanicoEntityActualizado.getTarifaHora());
                tx.commit();           // Hibernate detecta el cambio
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public int eliminar(Integer id) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                MecanicoEntity c = em.find(MecanicoEntity.class, id);
                if (c == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(c);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public List<Mecanico> buscarPorEspecialidad(String especialidad) {
        if (especialidad == null) {
            return List.of();
        }
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery(
                    "select m from MecanicoEntity e where e.especialidad = :especialidad",
                    MecanicoEntity.class).setParameter("especialidad", especialidad).getResultStream().map(MecanicoMapper::toDomain).toList();
        }
    }
}
