package com.maksimik.weather.model;

import java.io.Serializable;

public class Rain implements Serializable {

    private double mValue;

    public Rain(final double value) {
        this.mValue = value;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(final double value) {
        this.mValue = value;
    }

}
