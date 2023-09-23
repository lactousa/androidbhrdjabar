package com.example.bhrdjawabarat;
public class InfoHilalModel {

    private final String deskripsiInfoHilal;
    private String judulInfoHilal;
    private int gambarInfoHilal;
    private String isiInfoHilal;

    public InfoHilalModel(String judulInfoHilal, int gambarInfoHilal, String deskripsiInfoHilal) {
        this.judulInfoHilal = judulInfoHilal;
        this.gambarInfoHilal = gambarInfoHilal;
        this.deskripsiInfoHilal = deskripsiInfoHilal;
    }

    public String getJudulInfoHilal() {
        return judulInfoHilal;
    }

    public void setJudulInfoHilal(String judulInfoHilal) {
        this.judulInfoHilal = judulInfoHilal;
    }

    public int getGambarInfoHilal() {
        return gambarInfoHilal;
    }

    public void setGambarInfoHilal(int gambarInfoHilal) {
        this.gambarInfoHilal = gambarInfoHilal;
    }

    public String getIsiInfoHilal() {
        return isiInfoHilal;
    }

    public void setIsiInfoHilal(String isiInfoHilal) {
        this.isiInfoHilal = isiInfoHilal;
    }
}
