package com.ejemplo.catalogo.repository.file.csv;

import com.ejemplo.catalogo.model.Producto;

import java.util.List;
import java.util.Optional;

public interface RepositoryInterface {
    /**
     * Función que obtiene todos los elementos
     *
     * @return List de productos
     */
    List<Producto> findAll();

    /**
     * Función que obtiene el elemento por id
     * @param id identificador del producto
     * @return Optional del producto
     */

    Optional<Producto> findById(long id);

    /**
     * Crea el producto
     * @param producto
     */

    void create(Producto producto);

    /**
     * Actualiza el producto
     * @param producto
     * @return si se ha actualizado correctamente o no.
     */

    boolean update(Producto producto);

    /**
     * Función que elimina el producto por la id
     * @param id identificador del producto
     * @return si se ha eliminado correctamente o no.
     */

    boolean delete(long id);
}
