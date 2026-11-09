package es.ejemplo.biblioteca.dao;

import es.ejemplo.biblioteca.modelo.Socio;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import es.ejemplo.biblioteca.orm.entity.PrestamoEntity;
import es.ejemplo.biblioteca.orm.entity.SocioEntity;
import es.ejemplo.biblioteca.orm.mapper.SocioMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocioDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(SocioDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    public SocioDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    public SocioDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    public List<Socio> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SocioEntity s order by s.nombre", SocioEntity.class).getResultStream().map(SocioMapper::toDomain).toList();
        }
    }
    
    public Optional<Socio> buscarPorId(Integer id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(SocioEntity.class, id)).map(SocioMapper::toDomain);
        }
    }
    
    public int insertar(Socio dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SocioEntity entidad = SocioMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSocio(entidad.getIdSocio());
                tx.commit();
                return dato.getIdSocio();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                registrar("insertar", ex);
                return 0;
            }
        }
    }
    
    public int actualizar(Socio dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SocioEntity s = em.find(SocioEntity.class, dato.getIdSocio());
                if (s == null) {
                    tx.commit();
                    return 0;
                }
                SocioEntity socioEntityActualizado = SocioMapper.toEntity(dato);
                s.setNombre(socioEntityActualizado.getNombre());
                s.setEmail(socioEntityActualizado.getEmail());
                s.setTelefono(socioEntityActualizado.getTelefono());
                s.setFechaAlta(socioEntityActualizado.getFechaAlta());
                s.setActivo(socioEntityActualizado.getActivo());
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
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SocioEntity s = em.find(SocioEntity.class, id);
                if (s == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(s);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                registrar("eliminar", ex);
                throw ex;
            }
        }
    }
    private boolean validar(Socio dato) {return dato != null && dato.getNombre()!=null && !dato.getNombre().isBlank() && dato.getEmail()!=null && !dato.getEmail().isBlank() && dato.getFechaAlta()!=null && dato.getActivo()!=null;}
    
    public List<Socio> listarActivos() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SocioEntity s where s.activo = true", SocioEntity.class).getResultStream().map(SocioMapper::toDomain).toList();
        }
    }
    
    public Optional<Socio> buscarPorEmail(String email) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SocioEntity s where s.email = :email", SocioEntity.class).setParameter("email", email).getResultStream().map(SocioMapper::toDomain).findFirst();
        }
    }
    
    public List<Socio> listarConPrestamosVencidos(String fecha) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from PrestamoEntity p JOIN p.socio s where p.fechaLimite < :fecha", SocioEntity.class).setParameter("fecha", fecha).getResultStream().map(SocioMapper::toDomain).toList();
        }
    }
    
    public List<Socio> sociosSinPrestamos() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SocioEntity s where not exists (select p from PrestamoEntity p where p.idSocio = s.idSocio)", SocioEntity.class).getResultStream().map(SocioMapper::toDomain).toList();
        }
    }
    
    public List<Object[]> rankingSocios(int limite) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s, count(p) as total from PrestamoEntity p JOIN p.socio s group by s order by total desc limit :limite", Object[].class).setParameter("limite", limite).getResultStream().toList();
        }
    }

    // Historial completo de préstamos de un socio, indicando devoluciones y retrasos
    public List<Object[]> historialSocio(int idSocio) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s, p, l from PrestamoEntity p JOIN p.socio s JOIN LibroEntity l on p.idSocio = :idSocio order by p.fechaPrestamo", Object[].class).setParameter("idSocio", idSocio).getResultStream().toList();
        }
    }

    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
