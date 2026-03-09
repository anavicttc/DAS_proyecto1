package com.das.das_proyecto1;
public class Producto {
    private int id;
    private String nombre;
    public Producto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
}