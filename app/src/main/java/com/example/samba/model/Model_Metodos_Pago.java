package com.example.samba.model;

public class Model_Metodos_Pago {

    String titular, numero, caducidad, cvv, uid, id;
    long timestamp;

    public Model_Metodos_Pago() {
    }

    public Model_Metodos_Pago(String titular, String numero, String caducidad, String cvv, String uid, String id, long timestamp) {
        this.titular = titular;
        this.numero = numero;
        this.caducidad = caducidad;
        this.cvv = cvv;
        this.uid = uid;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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
}
