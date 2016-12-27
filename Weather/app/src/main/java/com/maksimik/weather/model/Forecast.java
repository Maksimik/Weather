package com.maksimik.weather.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Forecast implements Serializable {

    private City mCity;

    private ArrayList<DayWeather> mListWeatherHours;

    public Forecast() {
        mListWeatherHours = new ArrayList<>();
    }

    public City getCity() {
        return mCity;
    }

    public void setCity(final City city) {
        this.mCity = city;
    }

    public void add(final DayWeather dayWeather) {
        mListWeatherHours.add(dayWeather);
    }

    public DayWeather getDayWeather(final int i) {
        return mListWeatherHours.get(i);
    }

    public ArrayList<DayWeather> getListWeatherHours() {
        return mListWeatherHours;
    }

    public void setListWeatherHours(final ArrayList<DayWeather> listWeatherHours) {
        this.mListWeatherHours = listWeatherHours;
    }

}
