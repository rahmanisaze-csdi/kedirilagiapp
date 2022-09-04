package com.example.kedirilagi;

class Sejarahs {

    private String sejarahKey;
    private String nama;
    private String description;
    private String alamat;
    private String htm;
    private String open;
    private String highlight;
    private String picture;
    private String userId;
    private double latitude;
    private double longitude;
    private double distance;

    public Sejarahs(String nama, String description, String alamat, String htm, String open, String highlight, String picture, String userId) {
        this.nama = nama;
        this.description = description;
        this.alamat = alamat;
        this.htm = htm;
        this.open = open;
        this.highlight = highlight;
        this.picture = picture;
        this.userId = userId;
    }

    public Sejarahs() {
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getSejarahKey() {
        return sejarahKey;
    }

    public void setSejarahKey(String sejarahKey) {
        this.sejarahKey = sejarahKey;
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

    public String getHtm() {
        return htm;
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

    public void setHtm(String htm) {
        this.htm = htm;
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

