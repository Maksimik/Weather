package com.maksimik.weather.model;

public class Coord {

    private final double mLat;

    private final double mLon;

    public Coord(final double lat, final double lon) {
        this.mLon = lon;
        this.mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public double getLat() {
        return mLat;
    }

}
