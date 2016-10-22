package com.maksimik.weather.Data;


public class Coord {
    private double mLon;
    private double mLat;

    public Coord(double lon, double lan) {
        this.mLon = lon;
        this.mLat = lan;
    }


    public double getLon() {
        return mLon;
    }

    public void setLon(double mLon) {
        this.mLon = mLon;
    }

    public double getLan() {
        return mLat;
    }

    public void setLan(double mLan) {
        this.mLat = mLan;
    }
}
