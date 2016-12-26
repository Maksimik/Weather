package com.maksimik.weather.model;

import java.io.Serializable;

public class Snow implements Serializable {

    private double mValue;

    public Snow(double value) {
        this.mValue = value;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        this.mValue = value;
    }

}
