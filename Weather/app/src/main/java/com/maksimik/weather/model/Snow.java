package com.maksimik.weather.model;

import java.io.Serializable;

public class Snow implements Serializable {

    private double mValue;

    public Snow(final double value) {
        this.mValue = value;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(final double value) {
        this.mValue = value;
    }

}
