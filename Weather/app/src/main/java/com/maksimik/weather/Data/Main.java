package com.maksimik.weather.data;


import android.os.Parcel;
import android.os.Parcelable;

public class Main implements Parcelable {
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

    protected Main(Parcel in) {
        mTemp = in.readDouble();
        mTempMin = in.readDouble();
        mTempMax = in.readDouble();
        mPressure = in.readDouble();
        mHumidity = in.readDouble();
    }

    public static final Creator<Main> CREATOR = new Creator<Main>() {
        @Override
        public Main createFromParcel(Parcel in) {
            return new Main(in);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mTemp);
        dest.writeDouble(mTempMin);
        dest.writeDouble(mTempMax);
        dest.writeDouble(mPressure);
        dest.writeDouble(mHumidity);
    }
}