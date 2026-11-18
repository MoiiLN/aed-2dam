package aula.tareas.service;

import aula.tareas.modelo.Tarea;

import java.util.List;

public interface ITareaService {
    /**
     * Soy la documentación del método.
     * @return
     */
    public List<Tarea> listar();

    /**
     * soy la documentación de método.
     * @param id
     * @return
     */
    public Tarea buscar(long id);

    /**
     * Soy la documentación del método.
     * @param titulo
     * @return
     */
    public Tarea crear(String titulo);
}
