package ies.puesto.peliculas.service;

import ies.puesto.peliculas.model.Actor;
import ies.puesto.peliculas.model.DatosActor;

import java.util.List;

public interface IActorService {
    List<Actor> listar();
    Actor obtener(String idActor);
    Actor crear(String idActor, DatosActor datos);
    Actor editar(String idActor, DatosActor datos);
    void eliminar(String idActor);
}
