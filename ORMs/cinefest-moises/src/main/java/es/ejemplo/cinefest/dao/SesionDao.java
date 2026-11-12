package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sesion;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.SedeEntity;
import es.ejemplo.cinefest.orm.entity.SesionEntity;
import es.ejemplo.cinefest.orm.mapper.SedeMapper;
import es.ejemplo.cinefest.orm.mapper.SesionMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class SesionDao  {
    private final EntityManagerFactory managerFactory;
    public SesionDao(String jdbcUrl) {
        managerFactory=OrmFactory.crear(jdbcUrl);
    }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Sesion> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SesionEntity s", SesionEntity.class).getResultStream().map(SesionMapper::desdeEntidad).toList();
        }
    }
    public Optional<Sesion> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(SesionEntity.class, id)).map(SesionMapper::desdeEntidad);
        }
    }
    public int insertar(Sesion dato) {
        if (dato == null) {
            return 0;
        }
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SesionEntity entidad = SesionMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSesion(entidad.getIdSesion());
                tx.commit();
                return dato.getIdSesion();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                return 0;
            }
        }
    }
    public int actualizar(Sesion dato) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SesionEntity s = em.find(SesionEntity.class, dato.getIdSesion());
                if (s == null) {
                    tx.commit();
                    return 0;
                }
                SesionEntity sesionEntityActualizado = SesionMapper.aEntidad(dato);
                s.setFecha(sesionEntityActualizado.getFecha());
                s.setHora(sesionEntityActualizado.getHora());
                s.setPrecio(sesionEntityActualizado.getPrecio());
                s.setEstado(sesionEntityActualizado.getEstado());
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
                SesionEntity s = em.find(SesionEntity.class, id);
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
    public List<Object[]> sesionesConDetalles() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> sesionesPorFecha(String fecha) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> sesionesSinVentas() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> disponibilidadSesiones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> sesionesAgotadas() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> rankingRecaudacion() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> ingresosPorSede() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> informeDiario() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public int venderEntrada(int idSesion, int idEspectador, String fechaCompra) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
   
}
