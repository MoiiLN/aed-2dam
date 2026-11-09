package es.ejemplo.biblioteca.dao;
import es.ejemplo.biblioteca.modelo.Reserva;

import es.ejemplo.biblioteca.orm.entity.ReservaEntity;
import es.ejemplo.biblioteca.orm.mapper.ReservaMapper;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ReservaDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(ReservaDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    public ReservaDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    public ReservaDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    public List<Reserva> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r", ReservaEntity.class).getResultStream().map(ReservaMapper::toDomain).toList();
        }
    }
    
    public Optional<Reserva> buscarPorId(Integer id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(ReservaEntity.class, id)).map(ReservaMapper::toDomain);
        }
    }
    
    public int insertar(Reserva dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ReservaEntity entidad = ReservaMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdReserva(entidad.getIdReserva());
                tx.commit();
                return dato.getIdReserva();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                registrar("insertar", ex);
                return 0;
            }
        }
    }
    
    public int actualizar(Reserva dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ReservaEntity r = em.find(ReservaEntity.class, dato.getIdReserva());
                if (r == null) {
                    tx.commit();
                    return 0;
                }
                ReservaEntity reservaEntityActualizado = ReservaMapper.toEntity(dato);
                r.setEstado(reservaEntityActualizado.getEstado());
                r.setFechaReserva(reservaEntityActualizado.getFechaReserva());
                tx.commit();
                return 1;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    
    public int eliminar(Integer id) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ReservaEntity r = em.find(ReservaEntity.class, id);
                if (r == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(r);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    private boolean validar(Reserva dato) {return dato != null && dato.getIdSocio()!=null && dato.getIdLibro()!=null && dato.getFechaReserva()!=null && dato.getEstado()!=null && (dato.getEstado().equals("PENDIENTE") || dato.getEstado().equals("ATENDIDA") || dato.getEstado().equals("CANCELADA"));}
    
    public List<Reserva> listarPendientes() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r where r.estado = 'PENDIENTE'", ReservaEntity.class).getResultStream().map(ReservaMapper::toDomain).toList();
        }
    }
    
    public List<Reserva> buscarPorSocio(int idSocio) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r where r.idSocio = :idSocio", ReservaEntity.class).setParameter("idSocio", idSocio).getResultStream().map(ReservaMapper::toDomain).toList();
        }
    }
    
    public List<Reserva> listarPorLibro(int idLibro) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r where r.idLibro = :idLibro", ReservaEntity.class).setParameter("idLibro", idLibro).getResultStream().map(ReservaMapper::toDomain).toList();
        }
    }
    
    public long contarReservasPendientes() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select count(r) from ReservaEntity r where r.estado = 'PENDIENTE'", Long.class).getSingleResult();
        }
    }
    
    public List<Object[]> reservasConDetalles() {
        // TODO: implementar con EntityManager, JPQL y rollback cuando proceda.
        return List.of();
    }
    
    public List<Object[]> prioridadReservas() {
        // TODO: implementar con EntityManager, JPQL y rollback cuando proceda.
        return List.of();
    }


    
    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
