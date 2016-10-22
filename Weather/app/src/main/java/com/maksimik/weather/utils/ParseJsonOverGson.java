package com.maksimik.weather.utils;

import com.google.gson.Gson;
import com.maksimik.weather.Data.City;
import com.maksimik.weather.Data.Forecast;


public class ParseJsonOverGson {
    public Forecast parseJsonOverGson(String response) {
        Gson gson = new Gson();
        Forecast forecast=new Forecast();
        City city = gson.fromJson(response, City.class);
        forecast.setCity(city);
        return forecast;
    }
}
