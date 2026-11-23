package ies.puesto.peliculas.controller.api;

public record PeliculaRequest(String pelicula_codigo, String titulo_texto, int estreno_anio, int duracion_minutos) {}