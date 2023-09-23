package com.example.bhrdjawabarat;

public class PrayerTimes {
    private String fajr;

    private String shurooq;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;



    public PrayerTimes(String fajr, String shurooq,String dhuhr, String asr, String maghrib, String isha) {
        this.fajr = fajr;
        this.shurooq = shurooq;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
    }

    public String getFajr() {
        return fajr;
    }

    public String getShurooq() {return shurooq; }

    public String getDhuhr() {
        return dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public String getIsha() {
        return isha;
    }
}
