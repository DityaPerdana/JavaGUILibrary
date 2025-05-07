package com.example.model;

import java.util.Date;

public class Peminjaman {
    private int id;
    private String namaPeminjam;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String idBuku;
    private String judulBuku;
    
    public Peminjaman(int id, String namaPeminjam, Date tanggalPinjam, Date tanggalKembali, String idBuku) {
        this.id = id;
        this.namaPeminjam = namaPeminjam;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.idBuku = idBuku;
    }
    
    public Peminjaman() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNamaPeminjam() {
        return namaPeminjam;
    }
    
    public void setNamaPeminjam(String namaPeminjam) {
        this.namaPeminjam = namaPeminjam;
    }
    
    public Date getTanggalPinjam() {
        return tanggalPinjam;
    }
    
    public void setTanggalPinjam(Date tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }
    
    public Date getTanggalKembali() {
        return tanggalKembali;
    }
    
    public void setTanggalKembali(Date tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }
    
    public String getIdBuku() {
        return idBuku;
    }
    
    public void setIdBuku(String idBuku) {
        this.idBuku = idBuku;
    }
    
    public String getJudulBuku() {
        return judulBuku;
    }
    
    public void setJudulBuku(String judulBuku) {
        this.judulBuku = judulBuku;
    }
    
    public boolean isValidDateRange() {
        return tanggalPinjam != null && tanggalKembali != null && 
               !tanggalKembali.before(tanggalPinjam);
    }
}