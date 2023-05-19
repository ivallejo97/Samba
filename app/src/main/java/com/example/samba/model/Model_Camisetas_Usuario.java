package com.example.samba.model;

public class Model_Camisetas_Usuario {

    String titulo, talla, marca, precio, descripcion, fotoProducto, uid, id;
    long timestamp, numeroVisitas;

    public Model_Camisetas_Usuario() {
    }

    public Model_Camisetas_Usuario(String titulo, String talla, String marca, String precio, String descripcion, String fotoProducto, String uid, String id, long timestamp, long numeroVisitas) {
        this.titulo = titulo;
        this.talla = talla;
        this.marca = marca;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fotoProducto = fotoProducto;
        this.uid = uid;
        this.id = id;
        this.timestamp = timestamp;
        this.numeroVisitas = numeroVisitas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoProducto() {
        return fotoProducto;
    }

    public void setFotoProducto(String fotoProducto) {
        this.fotoProducto = fotoProducto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getNumeroVisitas() {
        return numeroVisitas;
    }

    public void setNumeroVisitas(long numeroVisitas) {
        this.numeroVisitas = numeroVisitas;
    }
}
