package com.example.gestinresiduos.clases;

public class Contenedores {

    private String id;
    private String color;
    private String capacidad;
    private String contenido;
    private String ubicacion;

    public String getCapacidad() {
        return capacidad;
    }

    public String getContenido() {
        return contenido;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }


}
