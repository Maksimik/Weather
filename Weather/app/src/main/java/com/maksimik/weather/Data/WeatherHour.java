package com.maksimik.weather.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherHour implements Parcelable {

    private long mDate;
    private Main mMain;
    private Weather mWeather;
    private Clouds mClouds;
    private Wind mWind;
    //private Rain mRain;
    //private Snow mSnow;


    protected WeatherHour(Parcel in) {
        mDate = in.readLong();
        mMain = in.readParcelable(Main.class.getClassLoader());
        mWeather = in.readParcelable(Weather.class.getClassLoader());
        mClouds = in.readParcelable(Clouds.class.getClassLoader());
        mWind = in.readParcelable(Wind.class.getClassLoader());
    }

    public static final Creator<WeatherHour> CREATOR = new Creator<WeatherHour>() {
        @Override
        public WeatherHour createFromParcel(Parcel in) {
            return new WeatherHour(in);
        }

        @Override
        public WeatherHour[] newArray(int size) {
            return new WeatherHour[size];
        }
    };

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public Main getMain() {
        return mMain;
    }

    //public WeatherHour(long date, Main main, Weather weather, Clouds clouds, Wind wind, Rain rain, Snow snow) {
    public WeatherHour(long date, Main main, Weather weather, Clouds clouds, Wind wind) {
        this.mDate = date;
        this.mMain = main;
        this.mWeather = weather;
        this.mClouds = clouds;
        this.mWind = wind;
        //this.mRain = rain;
        //this.mSnow = snow;
    }

    public void setMain(Main main) {
        this.mMain = main;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        this.mWeather = weather;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public void setClouds(Clouds clouds) {
        this.mClouds = clouds;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind wind) {
        this.mWind = wind;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mDate);
        dest.writeParcelable(mMain, flags);
        dest.writeParcelable(mWeather, flags);
        dest.writeParcelable(mClouds, flags);
        dest.writeParcelable(mWind, flags);
    }

    /*public Rain getRain() {
        return mRain;
    }

    public void setRain(Rain rain) {
        this.mRain = rain;
    }

    public Snow getSnow() {
        return mSnow;
    }

    public void setSnow(Snow snow) {
        this.mSnow = snow;
    }*/
}
