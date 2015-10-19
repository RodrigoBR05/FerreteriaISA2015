package com.example.breram.ferreteriaisa2015;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de datos estático para alimentar la aplicación
 */
public class Producto {
    private float precio;
    private String nombre;
    private int idDrawable;

    public Producto(float precio, String nombre, int idDrawable) {
        this.precio = precio;
        this.nombre = nombre;
        this.idDrawable = idDrawable;
    }

    public static final List<Producto> PRODUCTOS_POPULARES = new ArrayList<Producto>();

    static {
        PRODUCTOS_POPULARES.add(new Producto(3500, "Oferta", R.drawable.zegueta));
        PRODUCTOS_POPULARES.add(new Producto(30000, "Oferta", R.drawable.taladro));
        PRODUCTOS_POPULARES.add(new Producto(4000, "Oferta", R.drawable.llave));
        PRODUCTOS_POPULARES.add(new Producto(3000, "Oferta", R.drawable.llave2));
        PRODUCTOS_POPULARES.add(new Producto(5000, "Oferta", R.drawable.focos));
    }

    public float getPrecio() {
        return precio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdDrawable() {
        return idDrawable;
    }
}