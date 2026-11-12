package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sede;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.SalaEntity;
import es.ejemplo.cinefest.orm.entity.SedeEntity;
import es.ejemplo.cinefest.orm.mapper.SalaMapper;
import es.ejemplo.cinefest.orm.mapper.SedeMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class SedeDao {
    private final EntityManagerFactory managerFactory;
    public SedeDao(String jdbcUrl) {
        managerFactory=OrmFactory.crear(jdbcUrl);
    }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Sede> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SedeEntity s", SedeEntity.class).getResultStream().map(SedeMapper::desdeEntidad).toList();
        }
    }
    public Optional<Sede> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(SedeEntity.class, id)).map(SedeMapper::desdeEntidad);
        }
    }
    public int insertar(Sede dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SedeEntity entidad = SedeMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSede(entidad.getIdSede());
                tx.commit();
                return dato.getIdSede();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Sede dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SedeEntity s = em.find(SedeEntity.class, dato.getIdSede());
                if (s == null) {
                    tx.commit();
                    return 0;
                }
                SedeEntity sedeEntityActualizado = SedeMapper.aEntidad(dato);
                s.setNombre(sedeEntityActualizado.getNombre());
                s.setMunicipio(sedeEntityActualizado.getMunicipio());
                tx.commit();
                return 1;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int eliminar(int id) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SedeEntity s = em.find(SedeEntity.class, id);
                if (s == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(s);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public List<Object[]> salasPorSede(int idSede) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> aforoTotalPorSede() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    
}
