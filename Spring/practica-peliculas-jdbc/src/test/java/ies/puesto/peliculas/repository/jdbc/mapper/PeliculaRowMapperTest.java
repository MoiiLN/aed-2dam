
package ies.puesto.peliculas.repository.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PeliculaRowMapperTest {

    @Test
    void mapRow_convierteCorrectamente() throws SQLException {
        ResultSet rs = mock(ResultSet.class);

        when(rs.getString("pelicula_codigo")).thenReturn("PEL001");
        when(rs.getString("titulo_texto")).thenReturn("Interstellar");
        when(rs.getInt("estreno_anio")).thenReturn(2014);
        when(rs.getInt("duracion_minutos")).thenReturn(169);

        PeliculaRowMapper mapper = new PeliculaRowMapper();

        PeliculaEntity resultado = mapper.mapRow(rs, 0);

        assertAll(
                () -> assertEquals("PEL001", resultado.getCodigoFila()),
                () -> assertEquals("Interstellar", resultado.getTituloFila()),
                () -> assertEquals(2014, resultado.getAnioFila()),
                () -> assertEquals(169, resultado.getMinutosFila())
        );
    }
}
