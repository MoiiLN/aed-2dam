package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sala;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EntradaEntity;
import es.ejemplo.cinefest.orm.entity.SalaEntity;
import es.ejemplo.cinefest.orm.mapper.EntradaMapper;
import es.ejemplo.cinefest.orm.mapper.SalaMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class SalaDao {
    private final EntityManagerFactory managerFactory;
    public SalaDao(String jdbcUrl) {
        managerFactory=OrmFactory.crear(jdbcUrl);
    }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Sala> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e from SalaEntity e", SalaEntity.class).getResultStream().map(SalaMapper::desdeEntidad).toList();
        }
    }
    public Optional<Sala> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(SalaEntity.class, id)).map(SalaMapper::desdeEntidad);
        }
    }
    public int insertar(Sala dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SalaEntity entidad = SalaMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSala(entidad.getIdSala());
                tx.commit();
                return dato.getIdSala();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Sala dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SalaEntity s = em.find(SalaEntity.class, dato.getIdSala());
                if (s == null) {
                    tx.commit();
                    return 0;
                }
                SalaEntity salaEntityActualizado = SalaMapper.aEntidad(dato);
                s.setNombre(salaEntityActualizado.getNombre());
                s.setAforo(salaEntityActualizado.getAforo());
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
                SalaEntity s = em.find(SalaEntity.class, id);
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
    public List<Object[]> salasConSede() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Sala> salasSinSesiones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> ocupacionPorSala() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }

}
