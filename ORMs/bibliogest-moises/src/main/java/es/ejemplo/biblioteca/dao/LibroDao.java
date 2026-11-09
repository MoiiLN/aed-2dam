package es.ejemplo.biblioteca.dao;

import es.ejemplo.biblioteca.modelo.Libro;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import es.ejemplo.biblioteca.orm.entity.LibroEntity;
import es.ejemplo.biblioteca.orm.mapper.LibroMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibroDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(LibroDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    public LibroDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    public LibroDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    public List<Libro> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l order by l.titulo", LibroEntity.class).getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public Optional<Libro> buscarPorId(Integer id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(LibroEntity.class, id)).map(LibroMapper::toDomain);
        }
    }
    
    public int insertar(Libro dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                LibroEntity entidad = LibroMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdLibro(entidad.getIdLibro());
                tx.commit();
                return dato.getIdLibro();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                registrar("insertar", ex);
                return 0;
            }
        }
    }
    
    public int actualizar(Libro dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                LibroEntity l = em.find(LibroEntity.class, dato.getIdLibro());
                if (l == null) {
                    tx.commit();
                    return 0;
                }
                LibroEntity libroEntityActualizado = LibroMapper.toEntity(dato);
                l.setTitulo(libroEntityActualizado.getTitulo());
                l.setIsbn(libroEntityActualizado.getIsbn());
                l.setGenero(libroEntityActualizado.getGenero());
                l.setAnioPublicacion(libroEntityActualizado.getAnioPublicacion());
                l.setEjemplares(libroEntityActualizado.getEjemplares());
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
                LibroEntity l = em.find(LibroEntity.class, id);
                if (l == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(l);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    private boolean validar(Libro dato) {return dato != null && dato.getTitulo()!=null && !dato.getTitulo().isBlank() && dato.getIsbn()!=null && !dato.getIsbn().isBlank() && dato.getGenero()!=null && dato.getIdAutor()!=null && dato.getEjemplares()!=null && dato.getEjemplares()>=0;}
    
    public List<Libro> buscarPorGenero(String genero) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where l.genero = :genero", LibroEntity.class).setParameter("genero", genero).getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public List<Libro> buscarPorAutor(int idAutor) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where l.idAutor = :idAutor", LibroEntity.class).setParameter("idAutor", idAutor).getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public List<Libro> buscarPorTitulo(String texto) {
        if (texto == null || texto.isBlank()) {
            return List.of();
        }
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where l.titulo like :titulo", LibroEntity.class).setParameter("titulo", "%" + texto + "%").getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public List<Libro> listarDisponibles() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where l.ejemplares > 0", LibroEntity.class).getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public List<Libro> librosDesdeAnio(int anio, String genero) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where l.anioPublicacion >= :anio and l.genero = :genero", LibroEntity.class).setParameter("anio", anio).setParameter("genero", genero).getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public List<String> generos() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select distinct l.genero from LibroEntity l", String.class).getResultStream().toList();
        }
    }
    
    public List<Libro> nuncaPrestados() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where not exists (select p from PrestamoEntity p where p.idLibro = l.id)", LibroEntity.class).getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public List<Libro> masPrestadosQueMedia() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from PrestamoEntity p JOIN p.libro l", LibroEntity.class).getResultStream().map(LibroMapper::toDomain).toList();
        } //
    }
    
    public List<Libro> reservadosSinDisponibilidad() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from ReservaEntity r JOIN r.libro l where r.estado = 'PENDIENTE' and l.ejemplares = 0 group by l", LibroEntity.class).getResultStream().map(LibroMapper::toDomain).toList();
        }
    }
    
    public List<Object[]> librosConAutor() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l, a from LibroEntity l JOIN l.autor a", Object[].class).getResultStream().toList();
        }
    }
    
    public List<Object[]> contarPorGenero() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l.genero, count(l) from LibroEntity l group by l.genero", Object[].class).getResultStream().toList();
        }
    }
    
    public List<Object[]> prestamosPorGeneroMinimo(long minimo) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l.genero, count(p) from LibroEntity l JOIN PrestamoEntity p ON p.idLibro = l.idLibro group by l.genero having count(p) >= :minimo", Object[].class).setParameter("minimo", minimo).getResultStream().toList();
        }
    }
    
    public List<Object[]> disponibilidadPorLibro() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l, l.ejemplares, count(p), (l.ejemplares - count(p)) from LibroEntity l LEFT JOIN PrestamoEntity p ON p.idLibro = l.idLibro AND p.fechaDevolucion is null group by l", Object[].class).getResultStream().toList();
        }
    }
    
    public List<Object[]> estadisticasGenero() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select count(l), l.genero, l.ejemplares from LibroEntity l group by l.genero", Object[].class).getResultStream().toList();
        }
    }


    
    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
