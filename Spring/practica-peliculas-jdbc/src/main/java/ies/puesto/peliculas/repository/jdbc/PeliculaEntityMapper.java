package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import org.springframework.stereotype.Component;

@Component
public class PeliculaEntityMapper {

    public static PeliculaEntity aEntity(Pelicula pelicula) {
        return new PeliculaEntity(
                pelicula.getIdentificador(),
                pelicula.getNombre(),
                pelicula.getAnioLanzamiento(),
                pelicula.getMinutosProyeccion()
        );
    }

    public static Pelicula aPelicula(PeliculaEntity peliculaEntity) {
        return new Pelicula(
                peliculaEntity.getCodigoFila(),
                peliculaEntity.getTituloFila(),
                peliculaEntity.getAnioFila(),
                peliculaEntity.getMinutosFila()
        );
    }
}
