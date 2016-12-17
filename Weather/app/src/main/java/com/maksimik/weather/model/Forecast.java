package com.maksimik.weather.model;

import java.util.ArrayList;

public class Forecast {

    private City mCity;

    private ArrayList<DayWeather> mListWeatherHours;

    public Forecast() {
        mListWeatherHours = new ArrayList<>();
    }

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        this.mCity = city;
    }

    public void add(DayWeather dayWeather) {
        mListWeatherHours.add(dayWeather);
    }

    public DayWeather getDayWeather(int i) {
        return mListWeatherHours.get(i);
    }

    public ArrayList<DayWeather> getListWeatherHours() {
        return mListWeatherHours;
    }

    public int size(){
        return mListWeatherHours.size();
    }

    public void setListWeatherHours(ArrayList<DayWeather> listWeatherHours) {
        this.mListWeatherHours=listWeatherHours;
    }
}
