package ies.puesto.peliculas.mapper;

import ies.puesto.peliculas.controller.api.*;
import ies.puesto.peliculas.entity.ActorEntity;
import ies.puesto.peliculas.model.*;

public final class ActorMapper {
    private ActorMapper() {
    }

    public static Actor toDomain(ActorEntity fila) {
        return new Actor(fila.getIdActor(), fila.getNombreFila());
    }

    public static DatosActor RequestToDatos(ActorRequest request) {
        return new DatosActor(request.nombre());
    }

    public static ActorResponse toApi(Actor modelo) {
        return new ActorResponse(modelo.identificador(), modelo.nombre());
    }

    public static ActorEntity toEntity(String idActor, DatosActor datos) {
        return new ActorEntity(idActor, datos.nombre());
    }

    public static DatosActor toDatosActor(ActorEdicionRequest request) {
        return new DatosActor(request.nombre().trim());
    }
}
