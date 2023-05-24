package com.example.samba.model;

public class Model_Direcciones_Envio {

    String calle, numero, piso, puerta, pais, provincia, localidad, uid, id;
    long timestamp;

    public Model_Direcciones_Envio() {
    }

    public Model_Direcciones_Envio(String calle, String numero, String piso, String puerta, String pais, String provincia, String localidad, String uid, String id, long timestamp) {
        this.calle = calle;
        this.numero = numero;
        this.piso = piso;
        this.puerta = puerta;
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.uid = uid;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
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
