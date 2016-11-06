package com.maksimik.weather.Data;


import android.os.Parcel;
import android.os.Parcelable;

public class Wind implements Parcelable {
    private double mSpeed;
    private double mDeg;

    public Wind(double speed, double deg) {
        this.mSpeed = speed;
        this.mDeg = deg;
    }

    protected Wind(Parcel in) {
        mSpeed = in.readDouble();
        mDeg = in.readDouble();
    }

    public static final Creator<Wind> CREATOR = new Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mSpeed);
        dest.writeDouble(mDeg);
    }
}
