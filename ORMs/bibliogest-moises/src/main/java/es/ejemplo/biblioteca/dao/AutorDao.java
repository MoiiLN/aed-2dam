package es.ejemplo.biblioteca.dao;
import es.ejemplo.biblioteca.modelo.Autor;

import es.ejemplo.biblioteca.orm.entity.AutorEntity;
import es.ejemplo.biblioteca.orm.mapper.AutorMapper;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AutorDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(AutorDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    public AutorDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    public AutorDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    public List<Autor> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select a from AutorEntity a order by a.nombre", AutorEntity.class).getResultStream().map(AutorMapper::toDomain).toList();
        }
    }
    
    public Optional<Autor> buscarPorId(Integer id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(AutorEntity.class, id)).map(AutorMapper::toDomain);
        }
    }
    
    public int insertar(Autor dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                AutorEntity entidad = AutorMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdAutor(entidad.getIdAutor());
                tx.commit();
                return dato.getIdAutor();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                registrar("insertar", ex);
                return 0;
            }
        }
    }
    
    public int actualizar(Autor dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                AutorEntity a = em.find(AutorEntity.class, dato.getIdAutor());
                if (a == null) {
                    tx.commit();
                    return 0;
                }
                AutorEntity autorEntityActualizado = AutorMapper.toEntity(dato);
                a.setNombre(autorEntityActualizado.getNombre());
                a.setNacionalidad(autorEntityActualizado.getNacionalidad());
                a.setAnioNacimiento(autorEntityActualizado.getAnioNacimiento());
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
                AutorEntity c = em.find(AutorEntity.class, id);
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
    private boolean validar(Autor dato) {return dato != null && dato.getNombre()!=null && !dato.getNombre().isBlank();}
    
    public List<Autor> buscarPorNacionalidad(String nacionalidad) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select a from AutorEntity a where a.nacionalidad = :nacionalidad", AutorEntity.class).setParameter("nacionalidad", nacionalidad).getResultStream().map(AutorMapper::toDomain).toList();
        }
    }
    
    public long contarLibrosPorAutor(int idAutor) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select count(l) from LibroEntity l where l.idAutor = :idAutor", Long.class).setParameter("idAutor", idAutor).getSingleResult();
        }
    }
    
    public List<Object[]> autoresConRecuento() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select a, count(l) from AutorEntity a LEFT JOIN LibroEntity l ON l.autor.idAutor = a.idAutor group by a", Object[].class).getResultStream().toList();
        }
    }
    
    public List<Autor> autoresLeidosPorTresSocios() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select a from LibroEntity l join l.autor a join PrestamoEntity p on p.idLibro = l.idLibro group by a having count(distinct p.idSocio) >= 3", AutorEntity.class).getResultStream().map(AutorMapper::toDomain).toList();
        }
    }

    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
