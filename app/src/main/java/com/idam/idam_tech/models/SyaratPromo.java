package com.idam.idam_tech.models;

public class SyaratPromo {

    private String id,keterangan;
    int jumlah;

    public SyaratPromo() {
    }

    public SyaratPromo(String id, String keterangan, int jumlah) {
        this.id = id;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
