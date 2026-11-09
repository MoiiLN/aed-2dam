package es.ejemplo.biblioteca.dao;

import es.ejemplo.biblioteca.modelo.Prestamo;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import es.ejemplo.biblioteca.orm.entity.PrestamoEntity;
import es.ejemplo.biblioteca.orm.mapper.PrestamoMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrestamoDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(PrestamoDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    public PrestamoDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    public PrestamoDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    public List<Prestamo> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PrestamoEntity p", PrestamoEntity.class).getResultStream().map(PrestamoMapper::toDomain).toList();
        }
    }
    
    public Optional<Prestamo> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(PrestamoEntity.class, id)).map(PrestamoMapper::toDomain);
        }
    }
    
    public int insertar(Prestamo dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PrestamoEntity entidad = PrestamoMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdPrestamo(entidad.getIdPrestamo());
                tx.commit();
                return dato.getIdPrestamo();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                registrar("insertar", ex);
                return 0;
            }
        }
    }
    
    public int actualizar(Prestamo dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PrestamoEntity p = em.find(PrestamoEntity.class, dato.getIdPrestamo());
                if (p == null) {
                    tx.commit();
                    return 0;
                }
                PrestamoEntity prestamoEntityActualizado = PrestamoMapper.toEntity(dato);
                p.setFechaPrestamo(prestamoEntityActualizado.getFechaPrestamo());
                p.setFechaLimite(prestamoEntityActualizado.getFechaLimite());
                p.setFechaDevolucion(prestamoEntityActualizado.getFechaDevolucion());
                tx.commit();
                return 1;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                registrar("actualizar", e);
                return 0;
            }
        }
    }
    
    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PrestamoEntity p = em.find(PrestamoEntity.class, id);
                if (p == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(p);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                registrar("eliminar", ex);
                return 0;
            }
        }
    }
    private boolean validar(Prestamo dato) {return dato != null && dato.getIdSocio()!=null && dato.getIdLibro()!=null && dato.getFechaPrestamo()!=null && dato.getFechaLimite()!=null && dato.getFechaLimite().compareTo(dato.getFechaPrestamo())>=0 && (dato.getFechaDevolucion()==null || dato.getFechaDevolucion().compareTo(dato.getFechaPrestamo())>=0);}
    
    public List<Prestamo> listarPorSocio(int idSocio) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PrestamoEntity p where p.idSocio = :idSocio", PrestamoEntity.class).setParameter("idSocio", idSocio).getResultStream().map(PrestamoMapper::toDomain).toList();
        }
    }
    public List<Prestamo> listarActivos() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PrestamoEntity p where p.fechaDevolucion is null", PrestamoEntity.class).getResultStream().map(PrestamoMapper::toDomain).toList();
        }
    }
    
    public List<Prestamo> listarVencidos(String fecha) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PrestamoEntity p where p.fechaLimite < :fecha", PrestamoEntity.class).setParameter("fecha", fecha).getResultStream().map(PrestamoMapper::toDomain).toList();
        }
    }
    
    public long contarPrestamosPorLibro(int idLibro) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select count(p) from PrestamoEntity p where p.idLibro = :idLibro", Long.class).setParameter("idLibro", idLibro).getSingleResult();
        }
    }
    
    public List<Object[]> prestamosConSocioYLibro() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p, l, s from PrestamoEntity p JOIN p.libro l JOIN p.socio s", Object[].class).getResultStream().toList();
        }
    }
    
    public List<Object[]> rankingMensual(String anioMes, int limite) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l, a, count(p) from PrestamoEntity p join p.libro l join l.autor a where p.fechaPrestamo like :anioMes group by l, a order by count(p) desc ", Object[].class).setParameter("anioMes", anioMes + "%").setMaxResults(limite).getResultStream().toList();
        }
    }

    
    public int registrarPrestamo(int idSocio, int idLibro, String inicio, String limite) {
        Prestamo nuevoPrestamo = new Prestamo(
                null,
                idSocio,
                idLibro,
                inicio,
                limite,
                null
        );
        return insertar(nuevoPrestamo);
    }
    
    public int devolver(int id, String fecha) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PrestamoEntity p = em.find(PrestamoEntity.class, id);
                if (p == null) {
                    tx.commit();
                    return 0;
                }
                p.setFechaDevolucion(fecha);
                tx.commit();
                return 1;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                registrar("actualizar", e);
                return 0;
            }
        }
    }
    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
