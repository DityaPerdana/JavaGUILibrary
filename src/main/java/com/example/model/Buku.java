package com.example.model;

public class Buku {
    private String id;
    private String judul;
    private String pengarang;
    private int tahunTerbit;
    private String kategori;
    
    public Buku(String id, String judul, String pengarang, int tahunTerbit, String kategori) {
        this.id = id;
        this.judul = judul;
        this.pengarang = pengarang;
        this.tahunTerbit = tahunTerbit;
        this.kategori = kategori;
    }
    
    public Buku() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getJudul() {
        return judul;
    }
    
    public void setJudul(String judul) {
        this.judul = judul;
    }
    
    public String getPengarang() {
        return pengarang;
    }
    
    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }
    
    public int getTahunTerbit() {
        return tahunTerbit;
    }
    
    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }
    
    public String getKategori() {
        return kategori;
    }
    
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    
    @Override
    public String toString() {
        return id + " - " + judul + " (" + pengarang + ")";
    }
}