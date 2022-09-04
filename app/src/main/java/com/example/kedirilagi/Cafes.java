package com.example.kedirilagi;

public class Cafes {

    private String cafeKey;
    private String nama;
    private String description;
    private String alamat;
    private String hargaMenu;
    private String open;
    private String instagram;
    private String picture;
    private String userId;
    private String igLink;
    private double latitude;
    private double longitude;
    private double distance;

    public Cafes(String nama, String description, String alamat, String hargaMenu, String open, String instagram, String picture, String userId) {
        this.nama = nama;
        this.description = description;
        this.alamat = alamat;
        this.hargaMenu = hargaMenu;
        this.open = open;
        this.instagram = instagram;
        this.picture = picture;
        this.userId = userId;
    }

    public Cafes() {
    }

    public String getCafeKey() {
        return cafeKey;
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

    public String getHargaMenu() {
        return hargaMenu;
    }

    public String getOpen() {
        return open;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setCafeKey(String cafeKey) {
        this.cafeKey = cafeKey;
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

    public void setHargaMenu(String hargaMenu) {
        this.hargaMenu = hargaMenu;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIgLink() {
        return igLink;
    }

    public void setIgLink(String igLink) {
        this.igLink = igLink;
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

