package com.example.samba.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Model_Camisetas_Tienda {

    String titulo, talla, marca, precio, categoriaId, descripcion, url, uid, id, fotoCategoria;
    long timestamp, numeroVisitas;
    boolean favorito;

    public Model_Camisetas_Tienda() {
    }

    public Model_Camisetas_Tienda(String titulo, String talla, String marca, String precio, String categoriaId, String descripcion, String url, String uid, String id, String fotoCategoria, long timestamp, long numeroVisitas, boolean favorito) {
        this.titulo = titulo;
        this.talla = talla;
        this.marca = marca;
        this.precio = precio;
        this.categoriaId = categoriaId;
        this.descripcion = descripcion;
        this.url = url;
        this.uid = uid;
        this.id = id;
        this.fotoCategoria = fotoCategoria;
        this.timestamp = timestamp;
        this.numeroVisitas = numeroVisitas;
        this.favorito = favorito;
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

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getFotoCategoria() {
        return fotoCategoria;
    }

    public void setFotoCategoria(String fotoCategoria) {
        this.fotoCategoria = fotoCategoria;
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

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
