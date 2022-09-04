package com.example.kedirilagi;

public class ItineraryModel {

    private String key;
    private String nama;
    private String open;
    private String picture;
    private double latitude;
    private double longitude;
    private String userId;
    private int duration;
    private double distance;
    private String itineraryKey;
    private int waktu;
    private String time;

    public ItineraryModel(String key, String nama, String open, String picture, double latitude, double longitude, String userId, int duration) {
        this.key = key;
        this.nama = nama;
        this.open = open;
        this.picture = picture;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.duration = duration;
    }

    public ItineraryModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getItineraryKey() {
        return itineraryKey;
    }

    public void setItineraryKey(String itineraryKey) {
        this.itineraryKey = itineraryKey;
    }

    public int getWaktu() {
        return waktu;
    }

    public void setWaktu(int waktu) {
        this.waktu = waktu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


