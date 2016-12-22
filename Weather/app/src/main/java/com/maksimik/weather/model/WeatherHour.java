package com.maksimik.weather.model;

import java.io.Serializable;

public class WeatherHour implements Serializable {

    private long mDate;
    private Main mMain;
    private Weather mWeather;
    private Clouds mClouds;
    private Wind mWind;
    //private Rain mRain;
    //private Snow mSnow;


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
