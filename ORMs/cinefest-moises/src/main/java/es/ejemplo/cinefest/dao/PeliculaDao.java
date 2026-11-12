package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Pelicula;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EntradaEntity;
import es.ejemplo.cinefest.orm.entity.PeliculaEntity;
import es.ejemplo.cinefest.orm.mapper.EntradaMapper;
import es.ejemplo.cinefest.orm.mapper.PeliculaMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class PeliculaDao {
    private final EntityManagerFactory managerFactory;
    public PeliculaDao(String jdbcUrl) {
        managerFactory=OrmFactory.crear(jdbcUrl);
    }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Pelicula> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p", PeliculaEntity.class).getResultStream().map(PeliculaMapper::desdeEntidad).toList();
        }
    }
    public Optional<Pelicula> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(PeliculaEntity.class, id)).map(PeliculaMapper::desdeEntidad);
        }
    }
    public int insertar(Pelicula dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PeliculaEntity entidad = PeliculaMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdPelicula(entidad.getIdPelicula());
                tx.commit();
                return dato.getIdPelicula();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Pelicula dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PeliculaEntity p = em.find(PeliculaEntity.class, dato.getIdPelicula());
                if (p == null) {
                    tx.commit();
                    return 0;
                }
                PeliculaEntity peliculaEntityActualizado = PeliculaMapper.aEntidad(dato);
                p.setTitulo(peliculaEntityActualizado.getTitulo());
                p.setGenero(peliculaEntityActualizado.getGenero());
                p.setDuracionMin(peliculaEntityActualizado.getDuracionMin());
                p.setPais(peliculaEntityActualizado.getPais());
                p.setAnio(peliculaEntityActualizado.getAnio());
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
                PeliculaEntity p = em.find(PeliculaEntity.class, id);
                if (p == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(p);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public List<Pelicula> buscarPorGenero(String genero) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p where genero = :genero", PeliculaEntity.class).setParameter("genero", genero).getResultStream().map(PeliculaMapper::desdeEntidad).toList();
        }
    }
    public List<Pelicula> peliculasDesdeAnio(int anio) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p where anio >= :anio", PeliculaEntity.class).setParameter("anio", anio).getResultStream().map(PeliculaMapper::desdeEntidad).toList();
        }
    }
    public List<Pelicula> peliculasSinSesiones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p where not exists (select s from SesionEntity s where s.idPelicula = p.idPelicula)", PeliculaEntity.class).getResultStream().map(PeliculaMapper::desdeEntidad).toList();
        }
    }
    public List<Object[]> peliculasConMediaValoraciones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p, avg(v.puntuacion) from PeliculaEntity p join ValoracionEntity v on p.idPelicula = v.idPelicula group by p", Object[].class).getResultStream().toList();
        }
    }
    public List<Object[]> mejorValoradas(int minimo) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p, avg(v.puntuacion) from PeliculaEntity p join ValoracionEntity v on p.idPelicula = v.idPelicula group by p having avg(v.puntuacion) >= :minimo", Object[].class).setParameter("minimo", minimo).getResultStream().toList();
        }
    }
    public List<Object[]> masVendidasQueMedia() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p, s, e count from PeliculaEntity p join SesionEntity s on p.idPelicula = s.idPelicula join EntradaEntity e on p.idSesion = e.idSesion group by p having avg(s.) >= :minimo", Object[].class).getResultStream().toList();
        }
    }
    public List<Object[]> estadisticasGenero() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }

}
