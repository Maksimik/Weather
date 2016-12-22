package com.maksimik.weather.model;


public class Coord {

    private double mLat;

    private double mLon;

    public Coord(double lat, double lon) {
        this.mLon = lon;
        this.mLat = lat;
    }


    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        this.mLon = lon;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        this.mLat = lat;
    }
}
