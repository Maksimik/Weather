package com.maksimik.weather.data;


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

    public void setLon(double lon) {
        this.mLon = lon;
    }

    public double getLan() {
        return mLat;
    }

    public void setLan(double lan) {
        this.mLat = lan;
    }
}
