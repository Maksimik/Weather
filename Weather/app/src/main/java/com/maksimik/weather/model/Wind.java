package com.maksimik.weather.model;

import java.io.Serializable;

public class Wind implements Serializable {

    private final double mSpeed;
    private final double mDeg;

    public Wind(final double speed, final double deg) {
        this.mSpeed = speed;
        this.mDeg = deg;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public double getDeg() {
        return mDeg;
    }

}
