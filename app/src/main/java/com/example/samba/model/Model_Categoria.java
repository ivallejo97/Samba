package com.example.samba.model;

public class Model_Categoria {

    String id, categoria, descripcionCategoria,fotoCategoria, uid;
    long timestamp;

    public Model_Categoria(){}

    public Model_Categoria(String id, String categoria, String descripcionCategoria, String uid, String fotoCategoria , long timestamp ) {
        this.id = id;
        this.categoria = categoria;
        this.descripcionCategoria = descripcionCategoria;
        this.uid = uid;
        this.timestamp = timestamp;
        this.fotoCategoria = fotoCategoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFotoCategoria() {
        return fotoCategoria;
    }

    public void setFotoCategoria(String fotoCategoria) {
        this.fotoCategoria = fotoCategoria;
    }
}
