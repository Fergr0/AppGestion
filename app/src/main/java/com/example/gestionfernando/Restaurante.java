package com.example.gestionfernando;

import java.io.Serializable;
import java.util.Date;

public class Restaurante implements Serializable {
    private String nombre;
    private String descripcion;
    private int imagenUrl;
    private String direccionWeb;
    private String telefono;
    private boolean esFavorito;
    private float puntuacion;
    private Date fechaUltimaVisita;


    public Restaurante(String nombre, String descripcion, int imagenUrl, String direccionWeb, String telefono, boolean esFavorito, float puntuacion, Date fechaUltimaVisita) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.direccionWeb = direccionWeb;
        this.telefono = telefono;
        this.esFavorito = esFavorito;
        this.puntuacion = puntuacion;
        this.fechaUltimaVisita = fechaUltimaVisita;
    }


    // Getters y setters para la nueva propiedad
    public Date getFechaUltimaVisita() {
        return fechaUltimaVisita;
    }

    public void setFechaUltimaVisita(Date fechaUltimaVisita) {
        this.fechaUltimaVisita = fechaUltimaVisita;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(int imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getDireccionWeb() {
        return direccionWeb;
    }

    public void setDireccionWeb(String direccionWeb) {
        this.direccionWeb = direccionWeb;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }
}
