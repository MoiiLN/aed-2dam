package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Espectador;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EntradaEntity;
import es.ejemplo.cinefest.orm.entity.EspectadorEntity;
import es.ejemplo.cinefest.orm.mapper.EntradaMapper;
import es.ejemplo.cinefest.orm.mapper.EspectadorMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class EspectadorDao {
    private final EntityManagerFactory managerFactory;
    public EspectadorDao(String jdbcUrl) {
        managerFactory=OrmFactory.crear(jdbcUrl);
    }

    // Usar transacciones y rollback en operaciones de escritura.

    public List<Espectador> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e from EspectadorEntity e", EspectadorEntity.class).getResultStream().map(EspectadorMapper::desdeEntidad).toList();
        }
    }
    public Optional<Espectador> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(EspectadorEntity.class, id)).map(EspectadorMapper::desdeEntidad);
        }
    }
    public int insertar(Espectador dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EspectadorEntity entidad = EspectadorMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdEspectador(entidad.getIdEspectador());
                tx.commit();
                return dato.getIdEspectador();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Espectador dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EspectadorEntity a = em.find(EspectadorEntity.class, dato.getIdEspectador());
                if (a == null) {
                    tx.commit();
                    return 0;
                }
                EspectadorEntity espectadorEntityActualizado = EspectadorMapper.aEntidad(dato);
                a.setNombre(espectadorEntityActualizado.getNombre());
                a.setEmail(espectadorEntityActualizado.getEmail());
                a.setFechaAlta(espectadorEntityActualizado.getFechaAlta());
                a.setActivo(espectadorEntityActualizado.getActivo());
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
                EspectadorEntity e = em.find(EspectadorEntity.class, id);
                if (e == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(e);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public Optional<Espectador> buscarPorEmail(String email) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e from EspectadorEntity e where e.email = :email", EspectadorEntity.class).setParameter("email", email).getResultStream().findFirst().map(EspectadorMapper::desdeEntidad);
        }
    }
    public List<Espectador> espectadoresSinEntradas() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es from EspectadorEntity es where not exists (select e from EntradaEntity e)", EspectadorEntity.class).getResultStream().map(EspectadorMapper::desdeEntidad).toList();
        }
    }

    public List<Object[]> rankingEspectadores() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es, count(e) from EspectadorEntity es join EntradaEntity e on es.idEspectador = e.idEspectador where e.estado != 'CANCELADA' group by es order by count(e) desc, es.nombre", Object[].class).getResultStream().toList();
        }
    }
    public List<Object[]> espectadoresVariasSedes(int minimo) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es, count(distinct sed) from EspectadorEntity es join EntradaEntity e on e.idEspectador = es.idEspectador join SesionEntity ses on ses.idSesion = e.idSesion join SalaEntity sa on ses.idSala = sa.idSala join SedeEntity sed on sa.idSede = sed.idSede where e.estado != 'CANCELADA' group by es having count(distinct sed) >= 2 limit 5 order by count(sed) desc, es", Object[].class).getResultStream().toList();
        }
    }
}
