package com.example.gestinresiduos.clases;

public class Usuarios {

    private String username;
    private String password;
    private String rol;

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
