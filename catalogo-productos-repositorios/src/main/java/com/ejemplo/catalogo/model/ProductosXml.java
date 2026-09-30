package com.ejemplo.catalogo.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;
@JacksonXmlRootElement(localName = "productos")


public class ProductosXml {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "producto")
    private List<Producto> productos;


    public List<Producto> getProductos() {
        return productos;
    }

    public ProductosXml() {
        productos = new ArrayList<>();

    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
