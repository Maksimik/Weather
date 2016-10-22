package com.maksimik.weather.Data;

public class City {

    private int mId;
    private String mName;
    private String mCountry;
    private Coord mCcoord;

    /*public City(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country=country;
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
        return mCcoord;
    }

    public void setCoord(Coord coord) {
        this.mCcoord = coord;
    }
}
