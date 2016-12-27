package com.maksimik.weather.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DayWeather implements Serializable {

    private final List<WeatherHour> mDayWeather;

    public DayWeather() {
        this.mDayWeather = new ArrayList<>();
    }

    public void add(final WeatherHour weatherHour) {
        mDayWeather.add(weatherHour);
    }

    public WeatherHour getWeatherHour(final int i) {
        return mDayWeather.get(i);
    }

    public Iterable<WeatherHour> getDayWeather() {
        return mDayWeather;
    }

    public int size() {
        return mDayWeather.size();
    }

}
