package com.maksimik.weather.data;


import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("coord")
    private Coord mCoord;

    /*
    public City(int id, String name, String country) {
        this.mId = id;
        this.mName = name;
        this.mCountry = country;
    }*/


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public Coord getCoord() {
        return mCoord;
    }

    public void setCoord(Coord coord) {
        this.mCoord = coord;
    }
}
