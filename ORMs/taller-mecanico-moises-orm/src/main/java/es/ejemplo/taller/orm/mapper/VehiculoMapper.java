package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.Cliente;
import es.ejemplo.taller.modelo.Vehiculo;
import es.ejemplo.taller.orm.entity.ClienteEntity;
import es.ejemplo.taller.orm.entity.VehiculoEntity;

public class VehiculoMapper {
    public static VehiculoEntity toEntity(Vehiculo vehiculo) {
        return new VehiculoEntity(vehiculo.getMatricula(), vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getAnio(), vehiculo.getIdCliente());
    }

    public static Vehiculo toDomain(VehiculoEntity vehiculo) {
        return new Vehiculo(vehiculo.getMatricula(), vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getAnio(), vehiculo.getIdCliente());
    }
}
