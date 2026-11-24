package ies.puesto.peliculas.controller.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PeliculaRequest(String pelicula_codigo,
                              @Max(120)  String titulo_texto,
                              @Min(1888) @Max(2100) int estreno_anio,
                              @Min(1) @Max(1000) int duracion_minutos) {}