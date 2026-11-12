package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Entrada;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EntradaEntity;
import es.ejemplo.cinefest.orm.mapper.EntradaMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class EntradaDao  {
    private final EntityManagerFactory managerFactory;
    public EntradaDao(String jdbcUrl) {
        managerFactory=OrmFactory.crear(jdbcUrl);
    }
    // Usar transacciones y rollback en operaciones de escritura.
    public List<Entrada> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e from EntradaEntity e", EntradaEntity.class).getResultStream().map(EntradaMapper::desdeEntidad).toList();
        }
    }
    public Optional<Entrada> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(EntradaEntity.class, id)).map(EntradaMapper::desdeEntidad);
        }
    }
    public int insertar(Entrada dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity entidad = EntradaMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdEntrada(entidad.getIdEntrada());
                tx.commit();
                return dato.getIdEntrada();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Entrada dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity a = em.find(EntradaEntity.class, dato.getIdEntrada());
                if (a == null) {
                    tx.commit();
                    return 0;
                }
                EntradaEntity entradaEntityActualizado = EntradaMapper.aEntidad(dato);
                a.setFechaCompra(entradaEntityActualizado.getFechaCompra());
                a.setEstado(entradaEntityActualizado.getEstado());
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
                EntradaEntity e = em.find(EntradaEntity.class, id);
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
    // fecha_compra TEXT NOT NULL,
    // estado TEXT NOT NULL DEFAULT 'PAGADA' CHECK (estado IN ('PAGADA','USADA','CANCELADA')),
    public List<Object[]> entradasConDetalles() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e.fechaCompra, e.estado from EntradaEntity e", Object[].class).getResultStream().toList();
        }
    }

    public List<Object[]> entradasPorEspectador(int idEspectador) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es, e from EspectadorEntity es join EntradaEntity e where es.idEspectador = :idEspectador", Object[].class).setParameter("idEspectador", idEspectador).getResultStream().toList();
        }
    }

    public List<Object[]> ventasPorDia() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e.fechaCompra, count(e), sum(s.precio) from EntradaEntity e join SesionEntity s on e.idSesion = s.idSesion where e.estado != 'CANCELADA' group by e.fechaCompra order by e.fechaCompra", Object[].class).getResultStream().toList();
        }
    }

    public int cancelarEntrada(int idEntrada) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity a = em.find(EntradaEntity.class, idEntrada);
                if (a == null) {
                    tx.commit();
                    return 0;
                }
                a.setEstado("CANCELADA");
                tx.commit();
                return 1;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
}
