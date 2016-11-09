package com.maksimik.weather.data;

//TODO check for proper operation
public class Rain {
    private boolean mHas;
    private double mValue;

    /*public Rain(double value) {
        this.mValue = value;
    }*/

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        this.mValue = value;
    }
    public boolean isHas() {
        return mHas;
    }

    public void setHas(boolean has) {
        this.mHas = has;
    }
}
