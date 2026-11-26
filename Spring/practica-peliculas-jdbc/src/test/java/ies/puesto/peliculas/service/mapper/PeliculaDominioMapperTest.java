
package ies.puesto.peliculas.service.mapper;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeliculaDominioMapperTest {

    @Test
    void aPelicula_desdeDatosCreacion_convierteCorrectamente() {
        DatosCreacionPelicula datos = new DatosCreacionPelicula(
                "PEL001", "Interstellar", 2014, 169
        );

        Pelicula resultado = PeliculaDominioMapper.aPelicula(datos);

        assertAll(
                () -> assertEquals("PEL001", resultado.getIdentificador()),
                () -> assertEquals("Interstellar", resultado.getNombre()),
                () -> assertEquals(2014, resultado.getAnioLanzamiento()),
                () -> assertEquals(169, resultado.getMinutosProyeccion())
        );
    }

    @Test
    void aPelicula_desdeDatosEdicion_convierteCorrectamente() {
        DatosEdicionPelicula datos = new DatosEdicionPelicula(
                "Origen", 2010, 148
        );

        Pelicula resultado = PeliculaDominioMapper.aPelicula(
                "PEL002", datos
        );

        assertAll(
                () -> assertEquals("PEL002", resultado.getIdentificador()),
                () -> assertEquals("Origen", resultado.getNombre()),
                () -> assertEquals(2010, resultado.getAnioLanzamiento()),
                () -> assertEquals(148, resultado.getMinutosProyeccion())
        );
    }
}
