package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.Mecanico;
import es.ejemplo.taller.orm.entity.MecanicoEntity;

public class MecanicoMapper {

    public static MecanicoEntity toEntity(Mecanico mecanico) {
        return new MecanicoEntity(mecanico.getIdMecanico(), mecanico.getNombre(),
                mecanico.getEspecialidad(), mecanico.getTarifaHora());
    }

    public static Mecanico toDomain(MecanicoEntity entidad) {
        return new Mecanico(entidad.getIdMecanico(), entidad.getNombre(),
                entidad.getEspecialidad(), entidad.getTarifaHora());
    }
}
