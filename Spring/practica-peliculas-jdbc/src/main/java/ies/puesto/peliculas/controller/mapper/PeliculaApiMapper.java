package ies.puesto.peliculas.controller.mapper;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class PeliculaApiMapper {

    public static DatosCreacionPelicula aDatosCreacion(PeliculaRequest peliculaRequest) {
        return new DatosCreacionPelicula(
            peliculaRequest.pelicula_codigo(),
                peliculaRequest.titulo_texto(),
                peliculaRequest.estreno_anio(),
                peliculaRequest.duracion_minutos()
        );
    }

    public static DatosEdicionPelicula aDatosEdicion(PeliculaEdicionRequest peliculaEdicionRequest) {
        return new DatosEdicionPelicula(
            peliculaEdicionRequest.titulo(),
                peliculaEdicionRequest.anioEstreno(),
                peliculaEdicionRequest.duracionMinutos()
        );
    }

    public static PeliculaResponse aRespuesta(Pelicula pelicula) {
        return new PeliculaResponse(
                pelicula.getIdentificador(), pelicula.getNombre(),
                pelicula.getAnioLanzamiento(), pelicula.getMinutosProyeccion()
        );
    }

    public static List<PeliculaResponse> aRespuestas(List<Pelicula> peliculas) {
        ArrayList<PeliculaResponse> list = new ArrayList<>();
        for (Pelicula pelicula : peliculas) {
            list.add(aRespuesta(pelicula));
        }
        return list;
    }
}
