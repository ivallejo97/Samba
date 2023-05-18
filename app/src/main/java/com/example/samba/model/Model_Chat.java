package com.example.samba.model;

import java.io.Serializable;

public class Model_Chat implements Serializable {

    public String email, name, pais, profileImage, telefono, uid, userType, username;
    long timestamp;

    public Model_Chat() {
    }

    public Model_Chat(String email, String name, String pais, String profileImage, String telefono, String uid, String userType, String username, long timestamp) {
        this.email = email;
        this.name = name;
        this.pais = pais;
        this.profileImage = profileImage;
        this.telefono = telefono;
        this.uid = uid;
        this.userType = userType;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
