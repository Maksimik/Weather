package com.maksimik.weather.model;

import java.io.Serializable;

public class City implements Serializable {


    private int mId;

    private String mName;

    public City(int id, String name) {
        this.mId = id;
        this.mName = name;

    }

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

}
