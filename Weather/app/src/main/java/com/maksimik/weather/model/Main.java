package com.maksimik.weather.model;

import java.io.Serializable;

public class Main implements Serializable {

    private double mTemp;
    private double mTempMin;
    private double mTempMax;
    private double mPressure;
    private double mHumidity;

    public Main(final double temp, final double tempMin, final double tempMax, final double pressure, final double humidity) {
        this.mTemp = temp;
        this.mTempMin = tempMin;
        this.mTempMax = tempMax;
        this.mPressure = pressure;
        this.mHumidity = humidity;
    }

    public Main() {

    }

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(final int mTemp) {
        this.mTemp = mTemp;
    }

    public double getTempMin() {
        return mTempMin;
    }

    public void setTempMin(final double mTempMin) {
        this.mTempMin = mTempMin;
    }

    public double getTempMax() {
        return mTempMax;
    }

    public void setTempMax(final double mTempaMax) {
        this.mTempMax = mTempaMax;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(final double mPressure) {
        this.mPressure = mPressure;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(final double mHumidity) {
        this.mHumidity = mHumidity;
    }

}