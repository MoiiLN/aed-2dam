package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import es.codelearnacademy.filelab.repository.IRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ProductoService {

    private final IProductoRepository repository;

    public ProductoService(IProductoRepository repository) {
        this.repository = repository;
    }

    public Optional<Producto> maximoPrecio() {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto final_product = productos.getFirst();
        double max_precio = final_product.precio();
        for (Producto producto : productos) {
            if (producto.precio() > max_precio) {
                max_precio = producto.precio();
                final_product = producto;
            }
        }
        return Optional.of(final_product);
    }

    public Optional<Producto> minimoPrecio() {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto final_product = productos.getFirst();
        double min_price = final_product.precio();
        for (Producto producto : productos) {
            if (producto.precio() < min_price) {
                min_price = producto.precio();
                final_product = producto;
            }
        }
        return Optional.of(final_product);
    }

    public Optional<Producto> maximoStock() {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto final_product = productos.getFirst();
        int max_stock = final_product.stock();
        for (Producto producto : productos) {
            if (producto.stock() > max_stock) {
                max_stock = producto.stock();
                final_product = producto;
            }
        }
        return Optional.of(final_product);
    }

    public Optional<Producto> minimoStock() {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto final_product = productos.getFirst();
        int min_stock = final_product.stock();
        for (Producto producto : productos) {
            if (producto.stock() < min_stock) {
                min_stock = producto.stock();
                final_product = producto;
            }
        }
        return Optional.of(final_product);
    }

    public int stockTotal() {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return 0;
        }
        int total_stock = 0;
        for (Producto producto : productos) {
            total_stock += producto.stock();
        }
        return total_stock;
    }

    public double valorInventario() {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return 0;
        }
        double total_value = 0;
        for (Producto producto : productos) {
            total_value += producto.precio() * producto.stock();
        }
        return total_value;
    }

    public List<Producto> sinStock() {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return List.of();
        }
        List<Producto> out_of_stock = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.stock() == 0) {
                out_of_stock.add(producto);
            }
        }
        return out_of_stock;
    }

    public List<Producto> buscar(String texto) {
        List<Producto> productos = repository.findAll();
        if (productos.isEmpty()) {
            return List.of();
        }
        List<Producto> product_found = new ArrayList<>();
        texto = texto.toLowerCase(Locale.ROOT);
        for (Producto producto : productos) {
            if (producto.nombre().toLowerCase(Locale.ROOT).contains(texto)) {
                product_found.add(producto);
            }
        }
        return product_found;
    }
}
