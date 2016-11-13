package com.maksimik.weather.model;


import java.io.Serializable;

public class Clouds implements Serializable {

    private double mAll;

    public Clouds(double all) {
        this.mAll = all;
    }

    public double getAll() {
        return mAll;
    }

    public void setAll(double all) {
        this.mAll = all;
    }

}
