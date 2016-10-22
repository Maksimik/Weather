package com.maksimik.weather.Data;


import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lon")
    private double mLon;
    @SerializedName("lat")
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
