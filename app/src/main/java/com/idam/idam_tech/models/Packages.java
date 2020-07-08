package com.idam.idam_tech.models;

public class Packages {
    private String id;
    private String nama;
    private String harga;
    private String jumlah_hari;

    public Packages() {
    }

    public Packages(String id, String nama, String harga, String jumlah_hari) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.jumlah_hari = jumlah_hari;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }

    public String getJumlah_hari() {
        return jumlah_hari;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setJumlah_hari(String jumlah_hari) {
        this.jumlah_hari = jumlah_hari;
    }
}
