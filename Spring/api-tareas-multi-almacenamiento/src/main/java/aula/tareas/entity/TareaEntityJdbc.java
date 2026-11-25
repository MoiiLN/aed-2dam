package aula.tareas.entity;

import jakarta.persistence.*;

@Entity(name = "tareas_jdbc")
public record TareaEntityJdbc(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") Long idTarea,
                              @Column(name = "titulo") String nombreTarea) {}
