package com.example.bhrdjawabarat;

public class BeritaModel {

    private String judulBerita;
    private int gambarBerita;
    private String isiBerita;

    public BeritaModel(String judulBerita, int gambarBerita, String isiBerita) {
        this.judulBerita = judulBerita;
        this.gambarBerita = gambarBerita;
        this.isiBerita = isiBerita;
    }

    public static BeritaModel get() {
        return null;
    }

    public String getJudulBerita() {
        return judulBerita;
    }

    public void setJudulBerita(String judulBerita) {
        this.judulBerita = judulBerita;
    }

    public String getIsiBerita() {
        return isiBerita;
    }

    public int getGambarBerita() {
        return gambarBerita;
    }

    public void setGambarBerita(int gambarBerita) {
        this.gambarBerita = gambarBerita;
    }

    public void setIsiBerita(String isiBerita) {
        this.isiBerita = isiBerita;
    }
}
