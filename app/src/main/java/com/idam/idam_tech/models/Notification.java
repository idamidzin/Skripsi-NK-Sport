package com.idam.idam_tech.models;

public class Notification {
    private String id;
    private String nama;
    private String waktu;
    private String image;
    private String member_id;
    private String deskripsi;
    private String status;
    private String kategori;


    public Notification() {
    }

    public Notification(String id, String nama, String waktu, String image, String member_id, String deskripsi, String status, String kategori) {
        this.id = id;
        this.nama = nama;
        this.waktu = waktu;
        this.image = image;
        this.member_id = member_id;
        this.deskripsi = deskripsi;
        this.status = status;
        this.kategori = kategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
