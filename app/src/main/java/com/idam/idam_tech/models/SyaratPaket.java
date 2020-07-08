package com.idam.idam_tech.models;

public class SyaratPaket {

    private String id,keterangan;
    int jumlah;

    public SyaratPaket() {
    }

    public SyaratPaket(String id, String keterangan, int jumlah) {
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
