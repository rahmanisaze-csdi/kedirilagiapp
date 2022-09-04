package com.example.kedirilagi;

class Religis {

    private String religiKey;
    private String nama;
    private String description;
    private String alamat;
    private String open;
    private String highlight;
    private String picture;
    private String userId;
    private double latitude;
    private double longitude;
    private double distance;

    public Religis(String nama, String description, String alamat, String open, String highlight, String picture, String userId) {
        this.nama = nama;
        this.description = description;
        this.alamat = alamat;
        this.open = open;
        this.highlight = highlight;
        this.picture = picture;
        this.userId = userId;
    }

    public Religis() {
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getReligiKey() {
        return religiKey;
    }

    public void setReligiKey(String religiKey) {
        this.religiKey = religiKey;
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

    public String getHighlight() {
        return highlight;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
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

    public void setHighlight(String highlight) {
        this.highlight = highlight;
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

