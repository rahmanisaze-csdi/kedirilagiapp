package com.example.kedirilagi;

public class Pasars {

    private String pasarKey;
    private String nama;
    private String description;
    private String alamat;
    private String open;
    private String picture;
    private String userId;
    private double latitude;
    private double longitude;
    private double distance;

    public Pasars(String nama, String description, String alamat, String open, String picture, String userId) {
        this.nama = nama;
        this.description = description;
        this.alamat = alamat;
        this.open = open;
        this.picture = picture;
        this.userId = userId;
    }

    public Pasars() {
    }

    public String getPasarKey() {
        return pasarKey;
    }

    public String getNama() {
        return nama;
    }

    public String getDescription() {
        return description;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getOpen() {
        return open;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setPasarKey(String pasarKey) {
        this.pasarKey = pasarKey;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

