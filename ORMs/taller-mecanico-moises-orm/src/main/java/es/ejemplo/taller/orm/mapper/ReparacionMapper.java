package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.Reparacion;
import es.ejemplo.taller.orm.entity.ReparacionEntity;

public class ReparacionMapper {
    public static ReparacionEntity toEntity(Reparacion reparacion) {
        return new ReparacionEntity(reparacion.getIdMecanico(), reparacion.getNombre(),
                reparacion.getEspecialidad(), reparacion.getTarifaHora());
   }

    public static Reparacion toDomain(ReparacionEntity entidad) {
        return new ReparacionEntity(entidad.getIdMecanico(), entidad.getNombre(),
                entidad.getEspecialidad(), entidad.getTarifaHora());
    }
}
