package com.maksimik.weather.model;


import java.io.Serializable;

public class Main implements Serializable {
    private double mTemp;
    private double mTempMin;
    private double mTempMax;
    private double mPressure;
    private double mHumidity;

    public Main(double temp,double tempMin, double tempMax,double pressure, double humidity){
        this.mTemp=temp;
        this.mTempMin=tempMin;
        this.mTempMax=tempMax;
        this.mPressure=pressure;
        this.mHumidity=humidity;
    }
    public Main(){

    }

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(int mTemp) {
        this.mTemp = mTemp;
    }

    public double getTempMin() {
        return mTempMin;
    }

    public void setTempMin(double mTempMin) {
        this.mTempMin = mTempMin;
    }

    public double getTempMax() {
        return mTempMax;
    }

    public void setTempMax(double mTempaMax) {
        this.mTempMax = mTempaMax;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(double mPressure) {
        this.mPressure = mPressure;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double mHumidity) {
        this.mHumidity = mHumidity;
    }

}