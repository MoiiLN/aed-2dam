package aula.tareas.service;

import aula.tareas.api.TareaRequest;

import java.util.List;

public interface ITareaService {
    /**
     * Soy la documentación del método.
     * @return
     */
    public List<TareaRequest> listar();

    /**
     * soy la documentación de método.
     * @param id
     * @return
     */
    public TareaRequest buscar(long id);

    /**
     * Soy la documentación del método.
     * @param titulo
     * @return
     */
    public TareaRequest crear(String titulo);
}
