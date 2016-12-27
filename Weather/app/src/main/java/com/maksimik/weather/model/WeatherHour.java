package com.maksimik.weather.model;

import java.io.Serializable;

public class WeatherHour implements Serializable {

    private long mDate;
    private Main mMain;
    private Weather mWeather;
    private Clouds mClouds;
    private Wind mWind;
    private Rain mRain;
    private Snow mSnow;

    public long getDate() {
        return mDate;
    }

    public void setDate(final long date) {
        this.mDate = date;
    }

    public Main getMain() {
        return mMain;
    }

    public WeatherHour(final long date, final Main main, final Weather weather, final Clouds clouds, final Wind wind, final Rain rain, final Snow snow) {
        this.mDate = date;
        this.mMain = main;
        this.mWeather = weather;
        this.mClouds = clouds;
        this.mWind = wind;
        this.mRain = rain;
        this.mSnow = snow;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(final Weather weather) {
        this.mWeather = weather;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public Wind getWind() {
        return mWind;
    }

    public Rain getRain() {
        return mRain;
    }

    public Snow getSnow() {
        return mSnow;
    }

}
