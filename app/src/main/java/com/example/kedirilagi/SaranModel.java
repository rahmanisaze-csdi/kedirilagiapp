package com.example.kedirilagi;

public class SaranModel {

    private String saranKey;
    private String username;
    private String saran;
    private String picture;
    private String waktu;

    public SaranModel(String username, String saran, String picture, String waktu) {
        this.username = username;
        this.saran = saran;
        this.picture = picture;
        this.waktu = waktu;
    }

    public SaranModel() {
    }

    public String getSaranKey() {
        return saranKey;
    }

    public void setSaranKey(String saranKey) {
        this.saranKey = saranKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSaran() {
        return saran;
    }

    public void setSaran(String saran) {
        this.saran = saran;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
