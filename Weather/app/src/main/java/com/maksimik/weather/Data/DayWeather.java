package com.maksimik.weather.Data;


import java.util.ArrayList;

public class DayWeather {

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
    public int size(){
        return mDayWeather.size();
    }


}
