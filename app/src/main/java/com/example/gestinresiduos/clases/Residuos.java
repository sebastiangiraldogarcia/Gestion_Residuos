package com.example.gestinresiduos.clases;

public class Residuos {

    private String id;
    private String id_contenedor;
    private String user;
    private String material;
    private String liquido;
    private String peso;
    private String ubicacion;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getId_contenedor() {
        return id_contenedor;
    }

    public String getLiquido() {
        return liquido;
    }

    public String getMaterial() {
        return material;
    }

    public String getUser() {
        return user;
    }

    public void setId_contenedor(String id_contenedor) {
        this.id_contenedor = id_contenedor;
    }

    public void setLiquido(String liquido) {
        this.liquido = liquido;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

}
