
package ies.puesto.peliculas.controller.mapper;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeliculaApiMapperTest {

    @Test
    void aDatosCreacion_convierteCorrectamente() {
        PeliculaRequest request = new PeliculaRequest(
                "PEL001", "Interstellar", 2014, 169
        );

        DatosCreacionPelicula resultado =
                PeliculaApiMapper.aDatosCreacion(request);

        DatosCreacionPelicula esperado =
                new DatosCreacionPelicula("PEL001", "Interstellar", 2014, 169);

        assertEquals(esperado, resultado);
    }

    @Test
    void aDatosEdicion_convierteCorrectamente() {
        PeliculaEdicionRequest request = new PeliculaEdicionRequest(
                "Origen", 2010, 148
        );

        DatosEdicionPelicula resultado =
                PeliculaApiMapper.aDatosEdicion(request);

        DatosEdicionPelicula esperado =
                new DatosEdicionPelicula("Origen", 2010, 148);

        assertEquals(esperado, resultado);
    }

    @Test
    void aRespuesta_convierteCorrectamente() {
        Pelicula pelicula = new Pelicula(
                "PEL001", "Interstellar", 2014, 169
        );

        PeliculaResponse resultado = PeliculaApiMapper.aRespuesta(pelicula);

        PeliculaResponse esperado = new PeliculaResponse(
                "PEL001", "Interstellar", 2014, 169
        );

        assertEquals(esperado, resultado);
    }

    @Test
    void aRespuestas_convierteTodasLasPeliculas() {
        List<Pelicula> peliculas = List.of(
                new Pelicula("PEL001", "Interstellar", 2014, 169),
                new Pelicula("PEL002", "Origen", 2010, 148)
        );

        List<PeliculaResponse> resultado =
                PeliculaApiMapper.aRespuestas(peliculas);

        List<PeliculaResponse> esperado = List.of(
                new PeliculaResponse("PEL001", "Interstellar", 2014, 169),
                new PeliculaResponse("PEL002", "Origen", 2010, 148)
        );

        assertEquals(esperado, resultado);
    }

    @Test
    void aRespuestas_conListaVacia_devuelveListaVacia() {
        List<PeliculaResponse> resultado =
                PeliculaApiMapper.aRespuestas(List.of());

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
