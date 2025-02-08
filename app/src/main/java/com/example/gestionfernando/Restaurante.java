package com.example.gestionfernando;

import java.io.Serializable;
import java.util.Date;

public class Restaurante implements Serializable {
    private int id; // ID único para la base de datos
    private String nombre;
    private String descripcion;
    private String imagenUrl; // Cambiado de int a String
    private String direccionWeb;
    private String telefono;
    private boolean esFavorito;
    private float puntuacion;
    private Date fechaUltimaVisita;

    // Constructor con ID
    public Restaurante(int id, String nombre, String descripcion, String imagenUrl, String direccionWeb, String telefono, boolean esFavorito, float puntuacion, Date fechaUltimaVisita) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.direccionWeb = direccionWeb;
        this.telefono = telefono;
        this.esFavorito = esFavorito;
        this.puntuacion = puntuacion;
        this.fechaUltimaVisita = fechaUltimaVisita;
    }

    // Constructor sin ID (para inserciones en la base de datos)
    public Restaurante(String nombre, String descripcion, String imagenUrl, String direccionWeb, String telefono, boolean esFavorito, float puntuacion, Date fechaUltimaVisita) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.direccionWeb = direccionWeb;
        this.telefono = telefono;
        this.esFavorito = esFavorito;
        this.puntuacion = puntuacion;
        this.fechaUltimaVisita = fechaUltimaVisita;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
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

    public Date getFechaUltimaVisita() {
        return fechaUltimaVisita;
    }

    public void setFechaUltimaVisita(Date fechaUltimaVisita) {
        this.fechaUltimaVisita = fechaUltimaVisita;
    }
}
