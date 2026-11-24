package aula.tareas.entity;

import jakarta.persistence.*;

@Entity(name="tareas_orm") // Indicamos nombre de la tabla.
public class TareaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    public TareaEntity() {}

    public TareaEntity(Long id) {
        this.id = id;
    }

    public TareaEntity(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
