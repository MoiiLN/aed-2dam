package ies.puesto.peliculas.service.mapper;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.springframework.stereotype.Component;

@Component
public class PeliculaDominioMapper {

    public static Pelicula aPelicula(DatosCreacionPelicula datosCreacionPelicula) {
        return new Pelicula(
                datosCreacionPelicula.referenciaSolicitada(),
                datosCreacionPelicula.nombreSolicitado(),
                datosCreacionPelicula.anioSolicitado(),
                datosCreacionPelicula.minutosSolicitados()
        );
    }

    public static Pelicula aPelicula(String identificador, DatosEdicionPelicula datosEdicionPelicula) {
        return new Pelicula(
                identificador,
                datosEdicionPelicula.nombreSolicitado(),
                datosEdicionPelicula.anioSolicitado(),
                datosEdicionPelicula.minutosSolicitados()
        );
    }
}
