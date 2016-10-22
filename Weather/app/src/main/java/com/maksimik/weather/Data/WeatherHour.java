package com.maksimik.weather.Data;

/**
 * Created by 1 on 22.10.2016.
 */

public class WeatherHour {
    private Date mDate;
    private Main mMain;
    private Weather mWeather;
    private Clouds mClouds;
    private Wind mWind;
    private Rain mRain;
    private Snow mSnow;


    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public Main getMain() {
        return mMain;
    }

    public WeatherHour(Date date, Main main, Weather weather, Clouds clouds, Wind wind, Rain rain, Snow snow) {
        this.mDate = date;
        this.mMain = main;
        this.mWeather = weather;
        this.mClouds = clouds;
        this.mWind = wind;
        this.mRain = rain;
        this.mSnow = snow;
    }

    public void setMain(Main mMain) {
        this.mMain = mMain;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather mWeather) {
        this.mWeather = mWeather;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public void setClouds(Clouds mClouds) {
        this.mClouds = mClouds;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind mWind) {
        this.mWind = mWind;
    }

    public Rain getRain() {
        return mRain;
    }

    public void setRain(Rain mRain) {
        this.mRain = mRain;
    }

    public Snow getSnow() {
        return mSnow;
    }

    public void setSnow(Snow mSnow) {
        this.mSnow = mSnow;
    }
}
