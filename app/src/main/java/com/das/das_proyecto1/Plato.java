package com.das.das_proyecto1;

public class Plato {
    private int id;
    private String nombre;
    private String descripcion;
    private String fotoUri;
    public Plato(int id, String nombre, String descripcion, String fotoUri) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fotoUri=fotoUri;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getFotoUri() { return fotoUri; }
 }