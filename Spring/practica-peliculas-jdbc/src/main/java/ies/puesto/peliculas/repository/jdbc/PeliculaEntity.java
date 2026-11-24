package ies.puesto.peliculas.repository.jdbc;

public class PeliculaEntity {
    private final String codigoFila;
    private final String tituloFila;
    private final int anioFila;
    private final int minutosFila;

    public PeliculaEntity(String codigoFila, String tituloFila, int anioFila, int minutosFila) {
        this.codigoFila = codigoFila;
        this.tituloFila = tituloFila;
        this.anioFila = anioFila;
        this.minutosFila = minutosFila;
    }

    public String getCodigoFila() {
        return codigoFila;
    }

    public String getTituloFila() {
        return tituloFila;
    }

    public int getAnioFila() {
        return anioFila;
    }

    public int getMinutosFila() {
        return minutosFila;
    }
}