package com.idam.idam_tech.models;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class Histori {

    private String id;
    private String member_id;
    private String package_id;
    private String tanggal_mulai;
    private String tanggal_selesai;
    private String status_pembayaran;
    private String bukti_pembayaran;
    private String status_history;
    private String status_baca;
    private String nama;
    private String harga;

    public Histori() {
    }

    public Histori(String nama, String harga, String id, String member_id, String package_id, String tanggal_mulai, String tanggal_selesai, String status_pembayaran, String bukti_pembayaran, String status_history, String status_baca) {
        this.id = id;
        this.member_id = member_id;
        this.package_id = package_id;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_selesai = tanggal_selesai;
        this.status_pembayaran = status_pembayaran;
        this.bukti_pembayaran = bukti_pembayaran;
        this.status_history = status_history;
        this.status_baca = status_baca;
        this.nama = nama;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getTanggal_mulai() {
        return tanggal_mulai;
    }

    public void setTanggal_mulai(String tanggal_mulai) {
        this.tanggal_mulai = tanggal_mulai;
    }

    public String getTanggal_selesai() {
        return tanggal_selesai;
    }

    public void setTanggal_selesai(String tanggal_selesai) {
        this.tanggal_selesai = tanggal_selesai;
    }

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public void setStatus_pembayaran(String status_pembayaran) {
        this.status_pembayaran = status_pembayaran;
    }

    public String getBukti_pembayaran() {
        return bukti_pembayaran;
    }

    public void setBukti_pembayaran(String bukti_pembayaran) {
        this.bukti_pembayaran = bukti_pembayaran;
    }

    public String getStatus_history() {
        return status_history;
    }

    public void setStatus_history(String status_history) {
        this.status_history = status_history;
    }

    public String getStatus_baca() {
        return status_baca;
    }

    public void setStatus_baca(String status_baca) {
        this.status_baca = status_baca;
    }

}
