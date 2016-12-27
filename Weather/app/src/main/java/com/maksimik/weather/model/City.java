package com.maksimik.weather.model;

import java.io.Serializable;

public class City implements Serializable {

    private int mId;

    private String mName;

    public City(final int id, final String name) {
        this.mId = id;
        this.mName = name;

    }

    public int getId() {
        return mId;
    }

    public void setId(final int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        this.mName = name;
    }

}
