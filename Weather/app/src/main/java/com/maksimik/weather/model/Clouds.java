package com.maksimik.weather.model;

import java.io.Serializable;

public class Clouds implements Serializable {

    private final double mAll;

    public Clouds(final double all) {
        this.mAll = all;
    }

    public double getAll() {
        return mAll;
    }

}
