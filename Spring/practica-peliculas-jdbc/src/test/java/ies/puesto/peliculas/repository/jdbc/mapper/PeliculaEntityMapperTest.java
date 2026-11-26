
package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeliculaEntityMapperTest {

    @Test
    void aEntity_convierteCorrectamente() {
        Pelicula pelicula = new Pelicula(
                "PEL001", "Interstellar", 2014, 169
        );

        PeliculaEntity resultado = PeliculaEntityMapper.aEntity(pelicula);

        assertAll(
                () -> assertEquals("PEL001", resultado.getCodigoFila()),
                () -> assertEquals("Interstellar", resultado.getTituloFila()),
                () -> assertEquals(2014, resultado.getAnioFila()),
                () -> assertEquals(169, resultado.getMinutosFila())
        );
    }

    @Test
    void aPelicula_convierteCorrectamente() {
        Pelicula peliculaOriginal = new Pelicula(
                "PEL001", "Interstellar", 2014, 169
        );

        PeliculaEntity entity = PeliculaEntityMapper.aEntity(peliculaOriginal);

        Pelicula resultado = PeliculaEntityMapper.aPelicula(entity);

        assertAll(
                () -> assertEquals("PEL001", resultado.getIdentificador()),
                () -> assertEquals("Interstellar", resultado.getNombre()),
                () -> assertEquals(2014, resultado.getAnioLanzamiento()),
                () -> assertEquals(169, resultado.getMinutosProyeccion())
        );
    }
}
