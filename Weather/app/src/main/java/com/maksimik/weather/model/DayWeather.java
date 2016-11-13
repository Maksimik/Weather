package com.maksimik.weather.model;


import java.io.Serializable;
import java.util.ArrayList;

public class DayWeather implements Serializable {

    private ArrayList<WeatherHour> mDayWeather;

    public DayWeather() {
        this.mDayWeather = new ArrayList<>();
    }

    public void add(WeatherHour weatherHour){
        mDayWeather.add(weatherHour);
    }

    public WeatherHour getWeatherHour(int i){
        return mDayWeather.get(i);
    }

    public ArrayList<WeatherHour> getDayWeather(){
        return mDayWeather;
    }

    public int size(){
        return mDayWeather.size();
    }


}
