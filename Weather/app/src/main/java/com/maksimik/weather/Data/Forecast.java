package com.maksimik.weather.Data;

import java.util.ArrayList;

public class Forecast {
    private City mCity;
    private ArrayList<WeatherHour> mListWeatherHours;

    public Forecast() {
        mListWeatherHours = new ArrayList<WeatherHour>();
    }

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        this.mCity = city;
    }

    public void addWeatherHour(WeatherHour weatherHour) {
        mListWeatherHours.add(weatherHour);
    }

    public WeatherHour getWeatherHour(int i) {
        return mListWeatherHours.get(i);
    }
}
