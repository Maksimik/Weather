package com.maksimik.weather.Data;


public class Wind {
    private double mSpeed;
    private double mDeg;

    public Wind(double speed, double deg) {
        this.mSpeed = speed;
        this.mDeg = deg;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public double getDeg() {
        return mDeg;
    }

    public void setDeg(double deg) {
        this.mDeg = deg;
    }
}
