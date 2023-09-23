package com.example.bhrdjawabarat;

public class Berita {
    private String judul;
    private String deskripsi;
    private String gambarUrl;
    private String tanggal;

    // Konstruktor
    public Berita(String judul, String deskripsi, String gambarUrl, String tanggal) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.gambarUrl = gambarUrl;
        this.tanggal = tanggal;
    }

    // Getter dan Setter
    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }

    public String getTanggal() {
        return tanggal;
    }
}

