package aula.tareas.modelo;

public class Tarea {

    private long identificador;
    private String titulo;

    public Tarea(long identificador, String titulo) {
        this.identificador = identificador;
        this.titulo = titulo;
    }

    public long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(long identificador) {
        this.identificador = identificador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
