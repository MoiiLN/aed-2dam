package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Valoracion;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.SesionEntity;
import es.ejemplo.cinefest.orm.entity.ValoracionEntity;
import es.ejemplo.cinefest.orm.mapper.SesionMapper;
import es.ejemplo.cinefest.orm.mapper.ValoracionMapper;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class ValoracionDao {
    private final EntityManagerFactory managerFactory;

    public ValoracionDao(String jdbcUrl) {
        managerFactory=OrmFactory.crear(jdbcUrl);

     }
   
    // Usar transacciones y rollback en operaciones de escritura.

    public List<Valoracion> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select v from ValoracionEntity v", ValoracionEntity.class).getResultStream().map(ValoracionMapper::desdeEntidad).toList();
        }
    }
    public Optional<Valoracion> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(ValoracionEntity.class, id)).map(ValoracionMapper::desdeEntidad);
        }
    }
    public int insertar(Valoracion dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ValoracionEntity entidad = ValoracionMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdValoracion(entidad.getIdValoracion());
                tx.commit();
                return dato.getIdValoracion();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Valoracion dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ValoracionEntity v = em.find(ValoracionEntity.class, dato.getIdValoracion());
                if (v == null) {
                    tx.commit();
                    return 0;
                }
                ValoracionEntity valoracionEntityActualizado = ValoracionMapper.aEntidad(dato);
                v.setPuntuacion(valoracionEntityActualizado.getPuntuacion());
                v.setComentario(valoracionEntityActualizado.getComentario());
                v.setFecha(valoracionEntityActualizado.getFecha());
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
                ValoracionEntity v = em.find(ValoracionEntity.class, id);
                if (v == null) {
                    tx.commit();
                    return 0;
                }
                em.remove(v);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public List<Object[]> valoracionesConDetalles() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> peliculasSinValoraciones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> valoracionesPorPelicula(int idPelicula) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> estadisticasPuntuaciones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    
}
